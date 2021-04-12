package com.rs.game.player.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.rs.game.player.Player;

/**
 * @author `Discardedx2
 */
public final class Notes implements Serializable {

	private static final long serialVersionUID = 5564620907978487391L;

	/**
	 * The notes to display on the interface.
	 */
	private List<Note> notes = new ArrayList<Note>(30);

	/**
	 * The player.
	 */
	private transient Player player;

	public Notes(Player player) {
		this.player = player;
	}

	/**
	 * Unlocks the note interface
	 * @param player The player to unlock the note interface for.
	 */
	public static void unlock(Player player) {
		/*
		 * WorldPacketsEncoder encoder = player.getPackets();
		 * 
		 * encoder.sendIComponentSettings(34, 9, 0, 30, 2621470); encoder.sendHideIComponent(34, 3, false); encoder.sendHideIComponent(34, 44, false);
		 * 
		 * for (int i = 10; i < 16; i++) { encoder.sendHideIComponent(34, i, true); } player.getPackets().sendConfig(1439, -1);
		 * 
		 * for (int i = 1430; i < 1450; i++) { player.getPackets().sendConfig(i, i); }
		 * 
		 * player.getNotes().fullRefresh();
		 */
	}

	/**
	 * Fully refreshes the notes list.
	 * @param player The player to refresh for.
	 */
	public void fullRefresh() {
		if (!notes.isEmpty()) {
			for (int i = 149; i <= 178; i++) {
				Note note = notes.get(i - 149);
				if (note != null) {
					player.getPackets().sendGlobalString(i, note.text);
				}
			}
		}
		player.getPackets().sendConfig(1440, getPrimaryColour(this));
		player.getPackets().sendConfig(1441, getSecondaryColour(this));
	}

	/**
	 * Refreshes the current note.
	 * @return The note to refresh.
	 */
	public boolean refresh() {
		Note note = (Note) player.getTemporaryAttributtes().get("curNote");

		if (note == null) {
			return false;
		}

		player.getPackets().sendGlobalString(notes.indexOf(note), note.text);
		return true;
	}

	/**
	 * Adds a note.
	 * @param note The note to add.
	 * @return {@code true} if the note was added successfully.
	 */
	public boolean add(Note note) {
		if (notes.size() >= 30) {
			player.getPackets().sendGameMessage("You may only have 30 notes!", true);
			return false;
		}
		if (note.text.length() > 50) {
			player.getPackets().sendGameMessage("You can only enter notes up to 50 characters!", true);
			return false;
		}
		int id = notes.size();
		player.getPackets().sendConfig(1439, id);
		player.getPackets().sendGlobalString(149 + id, note.text);
		player.getTemporaryAttributtes().put("curNote", note);
		return notes.add(note);
	}

	/**
	 * Removes a note.
	 * @param note The note to remove.
	 * @return {@code true} if the note was removed successfully.
	 */
	public boolean remove(Note note) {
		int id = notes.indexOf(note);
		if (id < 0) {
			return false;
		}
		player.getPackets().sendConfig(1439, notes.size() - 2);
		player.getPackets().sendGlobalString(149 + id, "");
		player.getTemporaryAttributtes().put("curNote", notes.get(id - (id == 0 ? 0 : 1)));
		return notes.remove(note);
	}

	/**
	 * Gets the primary colour of the notes.
	 * @param notes The notes.
	 * @return
	 */
	public static int getPrimaryColour(Notes notes) {
		int color = 0;
		for (int i = 0; i < 15; i++) {
			if (notes.notes.size() > (i)) {
				color += colourize(notes.notes.get(i).colour, i);
			}
		}
		return color;
	}

	/**
	 * Gets the secondary colour of the notes.
	 * @param notes The notes.
	 * @return
	 */
	public static int getSecondaryColour(Notes notes) {
		int color = 0;
		for (int i = 0; i < 15; i++) {
			if (notes.notes.size() > (i + 16)) {
				color += colourize(notes.notes.get(i + 16).colour, i);
			}
		}
		return color;
	}

	/**
	 * Gets the colour value of a note.
	 * @param colour The colour.
	 * @param noteId The note id.
	 * @return The colour value.
	 */
	public static int colourize(int colour, int noteId) {
		return (int) (Math.pow(4, noteId) * colour);
	}

	/**
	 * Get the notes.
	 * @return the notes.
	 */
	public List<Note> getNotes() {
		return notes;
	}

	/**
	 * Represents a note on widget 34.
	 * @author `Discardedx2
	 */
	public static final class Note implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 9173992500345447484L;
		/**
		 * This note's text.
		 */
		private String text;
		/**
		 * This note's colour.
		 */
		private int colour;

		public Note(String text, int colour) {
			this.text = text;
			this.colour = colour;
		}

		public String getText() {
			return text;
		}

		public int getColour() {
			return colour;
		}

		public void setText(String text) {
			this.text = text;
		}

		public void setColour(int colour) {
			this.colour = colour;
		}
	}
}
