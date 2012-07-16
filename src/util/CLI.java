/*
 *
 * Copyright (c) 2010-2012,
 *
 *  Steve Suh           <suhsteve@gmail.com>
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The names of the contributors may not be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 *
 */

package util;

import java.io.File;
import java.util.*;

import org.apache.commons.cli.*;
import static java.lang.System.setProperty;
import static util.MyLogger.log;
import static util.MyLogger.LogLevel.DEBUG;
import static util.MyLogger.LogLevel.INFO;

public class CLI {

	private static Options options = new Options();;
	private static CommandLineParser parser = new PosixParser();
	private static CommandLine line;
	private static String filename;
	private static String classpath;
    private static final String USAGE = "[options] <.apk or .jar>";

	
	public static void parseArgs(String[] args, boolean reqArgs) {
		CLI.setupOptions();
		try {
			line = parser.parse(options, args);
		}
		catch (ParseException exp) {
			MyLogger.log(DEBUG, "Unexpected exception: " + exp.getMessage());
			System.err.println("Usage: " + USAGE);
			System.exit(0);
		}
		
    	if (CLI.hasOption("help")) {
    		HelpFormatter formatter = new HelpFormatter();
    		formatter.printHelp( USAGE, options );
    		System.exit(0);
    	}
    	
    	if (CLI.hasOption("verbose")) {    		
    		setProperty("LOG_LEVEL", line.getOptionValue("verbose"));
    	}
		
		fetchClasspath(reqArgs);
		fetchFilename();
		if (reqArgs && !(filename.endsWith(".apk") || filename.endsWith(".jar"))) {
			System.err.println("Usage: " + USAGE);
			System.exit(0);
		}
	}
	
	private static void setupOptions() {
		options.addOption("h", "help", false, "print this message" );
		options.addOption(OptionBuilder.withLongOpt( "verbose" ).withDescription( "set desired debugging outout level \"ERROR (0), WARNING (1), INFO (2), DEBUG (3)\"" ).hasArg().withArgName("level").create());
		options.addOption("c", "call-graph", false, "create call graph pdf");
		options.addOption("p", "partial-call-graph", false, "create partial call graph pdf");
		options.addOption("o", "one-leve-call-graph", false, "create one level call graph pdf");
		options.addOption("l", "include-library", false, "analyze library in flow analysis");
		options.addOption("s", "context-sensitive", false, "use context-sensitive 0-1-CFA");
		options.addOption("r", "model-reflection", false, "use built-in WALA reflection support");
		options.addOption("e", "separate-entries", false, "analyze each entry point separately");
		options.addOption("i", "IFDS-Explorer", false, "analyze each entry point separately");
		options.addOption(OptionBuilder.withLongOpt( "android-lib" ).withDescription( "use ALIB instead of default android lib" ).hasArg().withArgName("ALIB").create() );
		
	}
		
	public static boolean hasOption(String s) {
		return line != null && line.hasOption(s);
	}
	
	private static void fetchClasspath(boolean reqArgs) {
		//getArgs() returns all args that are not recognized;
		String [] myargs = line.getArgs();
		if ((myargs.length != 1 || !(myargs[0].endsWith(".apk") || myargs[0].endsWith(".jar"))) && reqArgs) {
			System.err.println("Usage: " + USAGE);
			System.exit(0);
		}
		if(myargs.length > 0)
		    classpath = myargs[0];
	}
	
	private static void fetchFilename() {
	    if(classpath == null) return;
		String[] path = classpath.split(File.separatorChar=='\\' ? "\\\\" : File.separator );
		if(path.length > 0)
		    filename = path[path.length-1];
	}
	
	public static String getClasspath() {
		return classpath;
	}
	
	public static String getFilename() {
		return filename;
	}
	
}