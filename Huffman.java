package huffman;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Huffman instances provide reusable Huffman Encoding Maps for compressing and
 * decompressing text corpi with comparable distributions of characters.
 */
public class Huffman {

	// -----------------------------------------------
	// Construction
	// -----------------------------------------------

	private HuffNode trieRoot;
	private Map<Character, String> encodingMap = new HashMap<Character, String>();

	/**
	 * Creates the Huffman Trie and Encoding Map using the character distributions
	 * in the given text corpus
	 * 
	 * @param corpus A String representing a message / document corpus with
	 *               distributions over characters that are implicitly used
	 *               throughout the methods that follow. Note: this corpus ONLY
	 *               establishes the Encoding Map; later compressed corpi may
	 *               differ.
	 */
	Huffman(String corpus) {
		HashMap<Character, Integer> distributions = new HashMap<Character, Integer>();
		// Created Hash map, iterated through corpus characters to find their
		// frequencies
		for (int i = 0; i < corpus.length(); i++) {
			if (distributions.containsKey(corpus.charAt(i))) {
				distributions.put(corpus.charAt(i), distributions.get(corpus.charAt(i)) + 1);
			} else {
				distributions.put(corpus.charAt(i), 1);
			}
		}
		// building the HuffmanTrie by creating priority queue and adding huffnode to
		// pQueue
		PriorityQueue<HuffNode> pQueue = new PriorityQueue<HuffNode>();
		for (Map.Entry<Character, Integer> entry : distributions.entrySet()) {
			HuffNode nodey = new HuffNode(entry.getKey(), entry.getValue());
			pQueue.add(nodey);
		}
		// Construction of the HuffmanTrie
		while (pQueue.size() > 1) {
			HuffNode smallestProb = pQueue.poll();
			HuffNode nextSmallest = pQueue.poll();
			HuffNode parentNode = new HuffNode('\0', smallestProb.count + nextSmallest.count);
			parentNode.left = smallestProb;
			parentNode.right = nextSmallest;
			pQueue.add(parentNode);
		}
		trieRoot = pQueue.poll();
//		System.out.println(trieRoot.right.character);
		encodingMap(trieRoot, "");
	}

	public void encodingMap(HuffNode n, String path) {
		if (n.isLeaf()) {
//			System.out.println(path);
			encodingMap.put(n.character, path);
			return;
		}
		if (!n.left.equals(null)) {
			encodingMap(n.left, path + "0");
		}
		if (!n.right.equals(null)) {
			encodingMap(n.right, path + "1");
		}
	}

	// -----------------------------------------------
	// Compression
	// -----------------------------------------------

	/**
	 * Compresses the given String message / text corpus into its Huffman coded
	 * bitstring, as represented by an array of bytes. Uses the encodingMap field
	 * generated during construction for this purpose.
	 * 
	 * @param message String representing the corpus to compress.
	 * @return {@code byte[]} representing the compressed corpus with the Huffman
	 *         coded bytecode. Formatted as 3 components: (1) the first byte
	 *         contains the number of characters in the message, (2) the bitstring
	 *         containing the message itself, (3) possible 0-padding on the final
	 *         byte.
	 */
	public byte[] compress(String message) {
		//uses the encodingMap
		int mLength = message.length();
		char[] mess = message.toCharArray();
		String bitString = "";
		for (int i = 0; i < mLength; i++) {
			bitString += encodingMap.get(mess[i]);
		}
		if (bitString.length() % 8 != 0) {
			
			
		}
		System.out.println("bitString:" + bitString);
		byte[] bite = {2};
		return bite;
		
		
	}

	// -----------------------------------------------
	// Decompression
	// -----------------------------------------------

	/**
	 * Decompresses the given compressed array of bytes into their original, String
	 * representation. Uses the trieRoot field (the Huffman Trie) that generated the
	 * compressed message during decoding.
	 * 
	 * @param compressedMsg {@code byte[]} representing the compressed corpus with
	 *                      the Huffman coded bytecode. Formatted as 3 components:
	 *                      (1) the first byte contains the number of characters in
	 *                      the message, (2) the bitstring containing the message
	 *                      itself, (3) possible 0-padding on the final byte.
	 * @return Decompressed String representation of the compressed bytecode
	 *         message.
	 */
	public String decompress(byte[] compressedMsg) {
		int outputLength = compressedMsg[0];
		String bitString = "";
		for (int i = 1; i < compressedMsg.length; i++) {
			bitString += String.format("%8s", (Integer.toBinaryString(compressedMsg[i] & 0xFF))).replace(' ', '0');
		}
		System.out.println(bitString);
		String outputString = "";

		// need a pointer that shows root
		int chars = 0;
		char[] bits = bitString.toCharArray();
		System.out.println(outputLength);

		HuffNode pointer = trieRoot;
		for (int i = 0; i < bitString.length() && chars < outputLength; i++) {
			if (pointer.isLeaf()) {
				outputString += Character.toString(pointer.character);
				chars++;
				pointer = trieRoot;
			}
			pointer = (bits[i] == '0') ? pointer.left : pointer.right;
		}

		return outputString;
	}

	// -----------------------------------------------
	// Huffman Trie
	// -----------------------------------------------

	/**
	 * Huffman Trie Node class used in construction of the Huffman Trie. Each node
	 * is a binary (having at most a left and right child), contains a character
	 * field that it represents (in the case of a leaf, otherwise the null character
	 * \0), and a count field that holds the number of times the node's character
	 * (or those in its subtrees) appear in the corpus.
	 */
	private static class HuffNode implements Comparable<HuffNode> {

		HuffNode left, right;
		char character;
		int count;

		HuffNode(char character, int count) {
			this.count = count;
			this.character = character;
		}

		public boolean isLeaf() {
			return left == null && right == null;
		}

		public int compareTo(HuffNode other) {
			return this.count - other.count;
		}

	}

}