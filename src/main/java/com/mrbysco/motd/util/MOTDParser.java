package com.mrbysco.motd.util;

import com.hypixel.hytale.server.core.Message;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class MOTDParser {
	private record State(Color color, boolean bold, boolean italic) {
		State with(Color c, Boolean b, Boolean i) {
			return new State(
					c != null ? c : color,
					b != null ? b : bold,
					i != null ? i : italic
			);
		}
	}

	private record TagState(String tag, State prev) {
	}

	public static Message parse(String input) {
		if (input == null || input.isEmpty()) return Message.raw("");

		int idx = 0;
		State current = new State(null, false, false);
		Deque<TagState> stack = new ArrayDeque<>();
		Message result = null;

		while (idx < input.length()) {
			int open = input.indexOf('<', idx);
			if (open == -1) {
				result = append(result, makeSegment(input.substring(idx), current));
				break;
			}

			if (open > idx)
				result = append(result, makeSegment(input.substring(idx, open), current));

			int close = input.indexOf('>', open + 1);
			if (close == -1) {
				result = append(result, makeSegment(input.substring(open), current));
				break;
			}

			String raw = input.substring(open + 1, close).trim();
			if (raw.isEmpty()) {
				idx = close + 1;
				continue;
			}

			if (raw.startsWith("/")) {
				String name = raw.substring(1).trim().toLowerCase();
				while (!stack.isEmpty()) {
					TagState ts = stack.pop();
					if (ts.tag.equals(name)) {
						current = ts.prev;
						break;
					}
				}
			} else {
				String[] parts = raw.split("\\s*=", 2);
				String tag = parts[0].toLowerCase();
				String attr = parts.length > 1 ? parts[1].trim() : "";

				stack.push(new TagState(tag, current));

				current = switch (tag) {
					case "b" -> current.with(null, true, null);
					case "i" -> current.with(null, null, true);
					case "color", "colour" -> current.with(parseColor(attr), null, null);
					default -> {
						stack.pop(); // unknown tag → revert immediately
						yield current;
					}
				};
			}

			idx = close + 1;
		}

		return result != null ? result : Message.raw("");
	}

	private static Color parseColor(String val) {
		try {
			if (val.isEmpty()) return null;
			if (!val.startsWith("#") && !val.startsWith("0x") && !val.startsWith("0X"))
				val = "#" + val;
			return Color.decode(val);
		} catch (Exception e) {
			return null;
		}
	}

	private static Message append(Message root, Message seg) {
		if (seg == null) return root;
		if (root != null) root.insert(seg);
		return root != null ? root : seg;
	}

	private static Message makeSegment(String text, State st) {
		if (text == null || text.isEmpty()) return null;
		Message msg = Message.raw(text)
				.bold(st.bold())
				.italic(st.italic());
		return st.color() != null ? msg.color(st.color()) : msg;
	}
}
