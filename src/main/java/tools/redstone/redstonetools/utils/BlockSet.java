package tools.redstone.redstonetools.utils;

import com.sk89q.worldedit.math.BlockVector3;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Membership set for block positions, backed by one BitSet per 16x16x16 chunk.
 * Used to track visited positions during a flood fill without boxing every coordinate.
 */
public class BlockSet {

	private static final int X_BITS = 4;
	private static final int Y_BITS = 4;
	private static final int Z_BITS = 4;
	private static final int CHUNK_SIZE = 1 << (X_BITS + Y_BITS + Z_BITS);

	/** Fixed-width fields, wide enough for world bounds and with no overlap. */
	private static final int X_CHUNK_BITS = 22;
	private static final int Y_CHUNK_BITS = 12;
	private static final int Z_CHUNK_BITS = 22;

	private final Map<Long, BitSet> chunks = new HashMap<>();

	public void add(BlockVector3 position) {
		chunkOf(position).set(bitOf(position));
	}

	public boolean contains(BlockVector3 position) {
		return chunkOf(position).get(bitOf(position));
	}

	private BitSet chunkOf(BlockVector3 position) {
		long x = (position.x() >> X_BITS) & ((1L << X_CHUNK_BITS) - 1);
		long z = (position.z() >> Z_BITS) & ((1L << Z_CHUNK_BITS) - 1);
		long y = (position.y() >> Y_BITS) & ((1L << Y_CHUNK_BITS) - 1);

		long key = (x << (Z_CHUNK_BITS + Y_CHUNK_BITS)) | (z << Y_CHUNK_BITS) | y;

		return chunks.computeIfAbsent(key, ignored -> new BitSet(CHUNK_SIZE));
	}

	private int bitOf(BlockVector3 position) {
		int x = position.x() & ((1 << X_BITS) - 1);
		int y = (position.y() & ((1 << Y_BITS) - 1)) << X_BITS;
		int z = (position.z() & ((1 << Z_BITS) - 1)) << (X_BITS + Y_BITS);

		return x | y | z;
	}
}
