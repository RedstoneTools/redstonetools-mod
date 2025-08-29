package tools.redstone.redstonetools.utils;

public class GuiUtils {

	public static Layout getWidgetLayout(int numberOfWidgets, double spacing, double totalWidth, int index, boolean includeEdgeSpacing, double edgeSpacing, double y, double height) {
		if (numberOfWidgets <= 0) throw new IllegalArgumentException("numberOfWidgets must be >= 1");
		if (index < 0 || index >= numberOfWidgets)
			throw new IndexOutOfBoundsException("index must be 0..numberOfWidgets-1");
		if (edgeSpacing < 0) throw new IllegalArgumentException("edgeSpacing must be >= 0");
		if (spacing < 0) throw new IllegalArgumentException("spacing must be >= 0");

		double totalBetweenSpacing = Math.max(0.0, (numberOfWidgets - 1) * spacing);
		double totalEdgeSpacing = includeEdgeSpacing ? 2.0 * edgeSpacing : 0.0;
		double totalSpacing = totalBetweenSpacing + totalEdgeSpacing;

		double availableForWidgets = totalWidth - totalSpacing;
		double width = Math.max(0.0, availableForWidgets / numberOfWidgets);

		double leftOffset = includeEdgeSpacing ? edgeSpacing : 0.0;
		double x = leftOffset + index * (width + spacing);

		return new Layout((int) x, (int) y, (int) width, (int) height);
	}

	public static class Layout {
		public final int x;
		public final int y;
		public final int width;
		public final int height;

		public Layout(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		@Override
		public String toString() {
			return "Layout{x=" + x + ", y= " + y + ", width=" + width + ", height=" + height + "}";
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Layout lo)) return super.equals(obj);
			return lo.x == x && lo.y == y && lo.height == height && lo.width == width;
		}
	}
}
