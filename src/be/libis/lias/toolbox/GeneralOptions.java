package be.libis.lias.toolbox;

import com.lexicalscope.jewel.cli.Option;

/**
 * The base interface that contains the general options.
 */
public interface GeneralOptions {

  @Option(shortName = { "?", "h" }, description = "Show this help text", helpRequest = true)
  boolean isHelp();

  @Option(longName = "log_level", description = "File logging level (0:none 1:error 2:warning 3:info 4:all - default 3)", defaultValue = "3", pattern = "[01234]")
  int getLogLevel();

  boolean isLogLevel();

  @Option(longName = "log_file", description = "Print logging to file")
  String getLogFile();

  boolean isLogFile();

  @Option(longName = "log_console_level", description = "Console logging level (0:none 1:error 2:warning 3:info 4:all - default:3)", defaultValue = "3", pattern = "[01234]")
  int getConsoleLogLevel();

  boolean isConsoleLogLevel();

  /*
   * @Option(description="Specifies SQLLite database file to log to") String
   * getLogDatabase(); boolean isLogDatabase();
   */

}
