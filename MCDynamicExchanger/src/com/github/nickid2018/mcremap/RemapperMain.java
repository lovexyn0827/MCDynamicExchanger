package com.github.nickid2018.mcremap;

import com.github.nickid2018.mcremap.argparser.*;

public class RemapperMain {

	public static ISystemLogger logger;

	public static void main(String[] args) throws Exception {
		logger = new DefaultConsoleLogger();
		try {
			CommandResult result = getResult(args);
			OfficalFormat format = new OfficalFormat(result);
			format.processInitMap(result.getSwitch("mapping_url").toString());
			logger.info("Generated class mapping");
			ClassTreeGenerator generator = new ClassTreeGenerator();
			generator.runGenerate(result, format);
			if (result.containsSwitch("-Dy")) {
				logger.info("Dry Run successfully.");
				return;
			}
			if (!(result.containsSwitch("--outmap") || result.containsSwitch("--outrev"))) {
				FileRemapper remapper = new FileRemapper();
				remapper.remapAll(result, format);
			}
		} catch (CommandParseException e) {
			RemapperMain.logger.error("Command is illegal!", e);
		} catch (Throwable e) {
			RemapperMain.logger.error("Unknown error has been thrown!", e);
		}
	}

	private static CommandResult getResult(String[] args) throws CommandParseException {
		CommandModel model = new CommandModel();
		UnorderSwitchTable table = new UnorderSwitchTable();
		model.switches.add(table);
		table.addLiteral(new LiteralSwitch("-Nh", true));// No hacks
		table.addLiteral(new LiteralSwitch("-D", true));// Detail Output
		table.addLiteral(new LiteralSwitch("-Dy", true));// Dry Run
		table.addSwitch("--output", new StringArgumentSwitch("--output"));// Output File
		table.addSwitch("--tmpdir", new StringArgumentSwitch("--tmpdir"));// Temporary Directory
		table.addSwitch("--outmap", new StringArgumentSwitch("--outmap"));// Output Formatted Output Map File
		table.addSwitch("--outrev", new StringArgumentSwitch("--outrev"));// Output Formatted Reverse Output Map File
																			// (For vanilla version mapping)
		model.switches.add(new StringSwitch("mapping_url"));// URL of mapping
		model.switches.add(new StringSwitch("mc_file"));// Minecraft Main JAR
		return model.parse(args);
	}
}
