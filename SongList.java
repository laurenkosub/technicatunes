package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeMap;

public class SongList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected TreeMap<String, String[]> songList;
	private final String[] letters = {"*", "T", "E", "C", "H", "N", "I", "K", "A", "!"};
	//private final String watch = "/watch?v=";
	//private final String embed = "/embed/";
	//private final String autoplay = "?autoplay=1";

	public SongList() {
		songList = new TreeMap<String, String[]>();
	}

	public void addSong(String link, String title, int count) {
		int letterIndex = (int) ((count - 1) / 10);
		int number = (int) ((count - 1) % 10);

		String buttonVal = letters[letterIndex] + number;

		//String url = link.replace(watch, embed);
		//link += autoplay;

		String[] info = {link, title };

		songList.put(buttonVal, info);
	}

	public void save(String fileName) throws Exception {
		File file = new File(fileName);

		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));

		output.writeObject(this);
		output.close();

	}

	public void load(String fileName) throws Exception {
		File file = new File(fileName);

		ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
		SongList savedList = (SongList) input.readObject();
		this.songList = savedList.songList;
		input.close();
	}
	
	public String getLink(String buttonValue){
		return(songList.get(buttonValue)[0]);
	}
	
	public String getTitle(String buttonValue){
		return(songList.get(buttonValue)[1]);
	}
	
	public String toString(){
		String list = "";
		Set<String> buttons = songList.keySet(); 
		for(String buttonVal: buttons){
			list+= buttonVal + ": " +  songList.get(buttonVal)[1] + "\n"; 
		}
		return(list);
	}
}
