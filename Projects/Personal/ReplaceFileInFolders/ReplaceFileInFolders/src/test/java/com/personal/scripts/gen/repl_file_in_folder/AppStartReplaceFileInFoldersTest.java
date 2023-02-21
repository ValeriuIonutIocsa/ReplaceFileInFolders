package com.personal.scripts.gen.repl_file_in_folder;

import org.junit.jupiter.api.Test;

class AppStartReplaceFileInFoldersTest {

	@Test
	void testMain() {

		final String[] args = {
				"D:\\p\\fo\\fpk\\800\\fofpk_0u0_800_1904\\work\\tools\\corema\\Settings\\CoreArchitectureSettings.xsd",
				"D:\\casdev",
				"D:\\p"
		};

		AppStartReplaceFileInFolders.main(args);
	}
}
