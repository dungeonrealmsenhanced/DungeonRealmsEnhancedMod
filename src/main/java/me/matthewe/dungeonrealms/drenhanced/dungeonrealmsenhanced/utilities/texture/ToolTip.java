package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.texture;

public class ToolTip {
	private int x, y;
	private String title;
	private String[] lines;
	private int padding;

	public ToolTip(int x, int y, String title, String[] lines, int padding) {
		super();
		this.x = x;
		this.y = y;
		this.title = title;
		this.lines = lines;
		this.padding = padding;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getTitle() {
		return title;
	}

	public String[] getLines() {
		return lines;
	}

	public int getPadding() {
		return padding;
	}
}