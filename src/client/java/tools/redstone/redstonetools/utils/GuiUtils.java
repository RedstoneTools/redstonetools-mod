package tools.redstone.redstonetools.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GuiUtils {
	public static List<Layout> betterGetWidgetLayout(List<MinMaxLayout> widgets, double spacing, double totalWidth, boolean includeEdgeSpacing, double edgeSpacing, double y, double height) {
		List<Layout> layouts = new ArrayList<>();
		if (widgets == null || widgets.isEmpty()) return layouts;

		int n = widgets.size();
		double totalBetweenSpacing = Math.max(0.0, (n - 1) * spacing);
		double totalEdgeSpacing = includeEdgeSpacing ? 2.0 * edgeSpacing : 0.0;
		double totalSpacing = totalBetweenSpacing + totalEdgeSpacing;

		double availableForWidgets = totalWidth - totalSpacing;
		// don't go negative for distribution calculations
		if (availableForWidgets < 0) availableForWidgets = 0;

		double[] min = new double[n];
		double[] max = new double[n];
		for (int i = 0; i < n; i++) {
			MinMaxLayout mm = widgets.get(i);
			min[i] = (mm.minWidth() == -1) ? 0.0 : mm.minWidth();
			max[i] = (mm.maxWidth() == -1) ? Double.POSITIVE_INFINITY : mm.maxWidth();
			if (max[i] < min[i]) max[i] = min[i]; // sanitize
		}

		double[] widths = new double[n];
		double sumMin = 0.0;
		for (int i = 0; i < n; i++) {
			widths[i] = min[i];
			sumMin += min[i];
		}

		double remaining = availableForWidgets - sumMin;

		if (remaining > 1e-9) {
			boolean[] finished = new boolean[n];
			int unfinishedCount = 0;
			for (int i = 0; i < n; i++) {
				finished[i] = (max[i] <= widths[i]); // already at cap
				if (!finished[i]) unfinishedCount++;
			}

			while (remaining > 1e-9 && unfinishedCount > 0) {
				double share = remaining / unfinishedCount;
				boolean madeProgress = false;
				for (int i = 0; i < n; i++) {
					if (finished[i]) continue;
					double canGrow = max[i] - widths[i];
					if (Double.isInfinite(canGrow) || canGrow > share) {
						widths[i] += share;
						remaining -= share;
					} else {
						widths[i] += canGrow;
						remaining -= canGrow;
						finished[i] = true;
						unfinishedCount--;
					}
					madeProgress = true;
				}
				if (!madeProgress) break;
			}
		}
		double xCursor = includeEdgeSpacing ? edgeSpacing : 0.0;
		for (int i = 0; i < n; i++) {
			int xi = (int) Math.round(xCursor);
			int yi = (int) Math.round(y);
			int wi = Math.max(0, (int) Math.round(widths[i]));
			int hi = (int) Math.round(height);
			layouts.add(new Layout(xi, yi, wi, hi));
			xCursor += widths[i] + spacing;
		}

		return layouts;
	}

	public record Layout(int x, int y, int width, int height) {
		@Override
		public @NotNull String toString() {
			return "Layout{x=" + x + ", y= " + y + ", width=" + width + ", height=" + height + "}";
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Layout(int x1, int y1, int width1, int height1))) return obj.equals(this);
			return x1 == x && y1 == y && height1 == height && width1 == width;
		}
	}
	// if any of these are -1, assume they aren't bound in that direction
	public record MinMaxLayout(int minWidth, int maxWidth, int minHeight, int maxHeight) {
		@Override
		public @NotNull String toString() {
			return "MinMaxLayout{minWidth=" + minWidth + ", maxWidth " + maxWidth + ", minHeight=" + minHeight + ", maxHeight=" + maxHeight + "}";
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof MinMaxLayout(int width, int width1, int height, int height1))) return obj.equals(this);
			return height == maxHeight &&
				width == maxWidth &&
				height1 == minHeight &&
				width1 == minWidth;
		}
	}
}
