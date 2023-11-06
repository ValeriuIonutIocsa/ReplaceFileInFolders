package com.personal.scripts.gen.repl_file_in_folder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

final class AppStartReplaceFileInFolders {

	private AppStartReplaceFileInFolders() {
	}

	public static void main(
			final String[] args) {

		final Instant start = Instant.now();

		if (args.length < 2) {

			final String helpMessage = createHelpMessage();
			System.err.println("ERROR - insufficient arguments" +
					System.lineSeparator() + helpMessage);
			System.exit(-1);
		}

		if ("-help".equals(args[0])) {

			final String helpMessage = createHelpMessage();
			System.out.println(helpMessage);
			System.exit(0);
		}

		Path filePath = Paths.get(args[0]);

		if (!Files.exists(filePath)) {
			System.err.println("ERROR - file " + filePath + " does not exist");
			System.exit(-2);

		} else if (!Files.isRegularFile(filePath)) {

			System.err.println("ERROR - " + filePath + " is not a file");
			System.exit(-3);
		}

		filePath = filePath.toAbsolutePath().normalize();

		final List<Path> folderPathList = new ArrayList<>();
		for (int i = 1; i < args.length; i++) {

			Path folderPath = Paths.get(args[i]);

			if (!Files.exists(folderPath)) {
				System.err.println("ERROR - folder " + folderPath + " does not exist");

			} else if (!Files.isDirectory(folderPath)) {
				System.err.println("ERROR - " + folderPath + " is not a directory");

			} else {
				folderPath = folderPath.toAbsolutePath().normalize();
				folderPathList.add(folderPath);
			}
		}
		if (folderPathList.isEmpty()) {
			System.exit(-4);
		}

		work(filePath, folderPathList);

		final Duration executionTime = Duration.between(start, Instant.now());
		System.out.println("done; execution time: " + durationToString(executionTime));
	}

	private static void work(
			final Path filePath,
			final List<Path> folderPathList) {

		System.out.println("--> starting ReplaceFileInFolders");

		System.out.println("file path:" + System.lineSeparator() + filePath);

		System.out.println("folder paths:");
		for (final Path folderPath : folderPathList) {
			System.out.println(folderPath);
		}

		final String fileName = filePath.getFileName().toString();
		for (final Path folderPath : folderPathList) {

			try {
				System.out.println("--> replacing files inside folder:" +
						System.lineSeparator() + folderPath);
				replaceFilesInFolder(folderPath, fileName, filePath);

			} catch (final Exception exc) {
				System.err.println("ERROR - failed to replace files in folder:" +
						System.lineSeparator() + folderPath +
						System.lineSeparator() + exc.getClass().getSimpleName() + " " + exc.getMessage());
			}
		}
	}

	private static void replaceFilesInFolder(
			final Path folderPath,
			final String fileName,
			final Path filePath) throws Exception {

		final List<Path> matchingFilePathList = new ArrayList<>();
		try (Stream<Path> filePathStream = Files.walk(folderPath)) {

			filePathStream
					.filter(aFilePath -> Files.isRegularFile(aFilePath) &&
							fileName.equals(aFilePath.getFileName().toString()) && !filePath.equals(aFilePath))
					.forEach(matchingFilePathList::add);
		}
		for (final Path matchingFilePath : matchingFilePathList) {

			System.out.println("replacing file: " + System.lineSeparator() + matchingFilePath);
			Files.setAttribute(matchingFilePath, "dos:readonly", false);
			Files.copy(filePath, matchingFilePath, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	private static String createHelpMessage() {

		return "usage: java replace_file_in_folders " +
				"<file-path> <folder-path-1> [<folder-path-2> ...]";
	}

	private static String durationToString(
			final Duration duration) {

		final StringBuilder stringBuilder = new StringBuilder();
		final long allSeconds = duration.get(ChronoUnit.SECONDS);
		final long hours = allSeconds / 3600;
		if (hours > 0) {
			stringBuilder.append(hours).append("h ");
		}

		final long minutes = (allSeconds - hours * 3600) / 60;
		if (minutes > 0) {
			stringBuilder.append(minutes).append("m ");
		}

		final long nanoseconds = duration.get(ChronoUnit.NANOS);
		final double seconds = allSeconds - hours * 3600 - minutes * 60 +
				nanoseconds / 1_000_000_000.0;
		stringBuilder.append(doubleToString(seconds)).append('s');

		return stringBuilder.toString();
	}

	private static String doubleToString(
			final double d) {

		final String str;
		if (Double.isNaN(d)) {
			str = "";

		} else {
			final String format;
			format = "0.000";
			final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
			final DecimalFormat decimalFormat = new DecimalFormat(format, decimalFormatSymbols);
			str = decimalFormat.format(d);
		}
		return str;
	}
}
