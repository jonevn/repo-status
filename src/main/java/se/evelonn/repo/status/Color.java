package se.evelonn.repo.status;

public enum Color {

	BLACK_FG("[30m"), BLACK_BG("[40m"), RED_FG("[31m"), RED_BG("[41m"), GREEN_FG("[32m"), GREEN_BG("[42m"),
	YELLOW_FG("[33m"), YELLOW_BG("[43m"), BLUE_FG("[34m"), BLUE_BG("[44m"), MAGENTA_FG("[35m"), MAGENTA_BG("[45m"),
	CYAN_FG("[36m"), CYAN_BG("[46m"), WHITE_FG("[37m"), WHITE_BG("[47m");

	private static char ESCAPE_CHARACTER = (char) 27;
	private static String NO_COLOR = "[0m";

	private String color;

	private Color(String color) {
		this.color = color;
	}

	public String colorize(String string) {
		return ESCAPE_CHARACTER + color + string + ESCAPE_CHARACTER + NO_COLOR;
	}
}