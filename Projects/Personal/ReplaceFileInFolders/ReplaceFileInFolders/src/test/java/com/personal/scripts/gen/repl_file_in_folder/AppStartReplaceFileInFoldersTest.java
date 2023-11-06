package com.personal.scripts.gen.repl_file_in_folder;

import org.junit.jupiter.api.Test;

class AppStartReplaceFileInFoldersTest {

	@Test
	void testMain() {

		final String[] args;
		final int input = Integer.parseInt("1");
		if (input == 1) {
			args = new String[] {
					"C:\\IVI\\Prog\\JavaGradle\\Scripts\\General\\UtilsManager\\common_build.gradle",
					"C:\\IVI"
			};

		} else if (input == 2) {
			args = new String[] {
					"D:\\p\\fo\\fpk\\800\\fofpk_0u0_800_1904\\" +
							"work\\tools\\corema\\Settings\\CoreArchitectureSettings.xsd",
					"D:\\casdev",
					"D:\\p"
			};

		} else {
			throw new RuntimeException();
		}

		AppStartReplaceFileInFolders.main(args);
	}
}
