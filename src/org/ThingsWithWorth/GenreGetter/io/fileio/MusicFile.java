package org.ThingsWithWorth.GenreGetter.io.fileio;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import org.ThingsWithWorth.Nicities.FileIO.FormattedFiles.ExtendedFile;
import org.ThingsWithWorth.Nicities.FileIO.FormattedFiles.SmartFile;
import org.ThingsWithWorth.Nicities.FileIO.FormattedFiles.SmartFileFactory;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileFilter;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class MusicFile extends SmartFile {
	public static final String TYPE = "Music File";
	public static final String ERROR_NO_TAG_ACCESS = "Cannot access tag data.";
	public static final String ERROR_AUDIOTAGGER_ERROR = "Audiotagger is not working prorperly.";
	public static final String ERROR_WRONG_DATA_ERROR = "Cant set tag to desired value.";
	public static final String[] EXTENSIONS = { "flac", "m4a", "m4b", "m4p", "mp3", "mp4", "ogg", "ra", "rm", "wav", "wmv" };
	private AudioFile tagsFile;
	private Tag myTag;

	protected MusicFile(File f) {
		super(f);
		if ((new AudioFileFilter()).accept(f)) {
			try {
				tagsFile = AudioFileIO.read(f);
				myTag = tagsFile.getTag();
			} catch (CannotReadException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TagException e) {
				e.printStackTrace();
			} catch (ReadOnlyFileException e) {
				e.printStackTrace();
			} catch (InvalidAudioFrameException e) {
				e.printStackTrace();
			}
		}
	}

	protected MusicFile(String s) {
		this(new File(s));
	}

	public Tag getTagData() {
		if (tagsFile != null) {
			return tagsFile.getTag();
		} else {
			return null;
		}
	}

	public String setFileGenre(String setTo) {
		if (tagsFile != null) {
			try {
				myTag.setField(FieldKey.GENRE, setTo);
				tagsFile.setTag(myTag);
			} catch (KeyNotFoundException e) {
				e.printStackTrace();
				return ERROR_AUDIOTAGGER_ERROR;
			} catch (FieldDataInvalidException e) {
				e.printStackTrace();
				return ERROR_WRONG_DATA_ERROR;
			}
			return null;
		} else {
			return ERROR_NO_TAG_ACCESS;
		}

	}

	@Override
	public String[] getValidExtensionRegex() {
		return EXTENSIONS;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public boolean isReadable() {
		return false;
	}

	@Override
	public SmartFileFactory getFactory() {
		return new MusicFileFactory();
	}

	public static class MusicFileFactory extends SmartFileFactory {
		public final static MusicFileFactory instance = new MusicFileFactory();

		protected SmartFile makeSmarter(File make) {
			return new MusicFile(make);
		}

		public static MusicFile[] makeSmarter(File[] makeSmart) {
			ArrayList<File> valid = new ArrayList<File>();
			for (File f : makeSmart) {
				if (instance.isValid(f))
					valid.add(new MusicFile(f));
			}
			return valid.toArray(new MusicFile[valid.size()]);
		}

		@Override
		public boolean isValid(File file) {
			if (file != null)
				for (String exten : EXTENSIONS)
					if ((new ExtendedFile(file)).getExtension().equals(exten))
						return true;

			return false;
		}
	}

	public static class AlbumFolderFilter implements FileFilter {

		@Override
		public boolean accept(File file) {
			if (file.isDirectory()) {

				File[] musicFiles = file.listFiles(new FileFilter() {
					@Override
					public boolean accept(File file) {
						return MusicFileFactory.instance.isValid(file);
					}
				});
				return musicFiles.length > 0;
			}
			return false;
		}

	}
}
