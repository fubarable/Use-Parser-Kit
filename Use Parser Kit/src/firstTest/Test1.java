
package firstTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.rtfparserkit.converter.text.StreamTextConverter;
import com.rtfparserkit.parser.RtfListenerAdaptor;
import com.rtfparserkit.parser.RtfStreamSource;
import com.rtfparserkit.parser.raw.RawRtfParser;
import com.rtfparserkit.rtf.Command;

public class Test1 {
    private static final String[] CMD_IGNORES = { "brdrs", "brdrw", "clbrdrl", "trpaddfl", "drpaddfr", "sa", "sl",
	    "slmult", "f", "lang", "trgph", "clvertalc", "clcbpat", "qc", "fs", "trgaph", "clbrdrr", "brdrcf",
	    "clbrdrb", "itap", "optionalcommand", "clbrdrt", "cellx", "trpaddl", "trpaddr", "trpaddfr", "rtf", "ansi",
	    "ansicpg", "deff", "nouicompat", "deflang", "fonttbl", "fnil", "fswiss", "fcharset", "froman", "colortbl",
	    "red", "green", "blue", "generator", "viewkind", "uc", "*" };
    private static final String TXT_SRC = "data/testTextConversion.txt";
    private static final String RTF_SRC1 = "data/testTextConversion.rtf";
    private static final String RTF_SRC2 = "data/TestFaceSheet.rtf";
    private static final String OUTFILE_NAME = "Test1Out.txt";
    private static List<String> cmdIgnoreList = new ArrayList<>();
    private static List<String> cmdList = new ArrayList<>();

    static {
	for (String cmdIgnore : CMD_IGNORES) {
	    cmdIgnoreList.add(cmdIgnore);
	}
    }

    public static void main(String[] args) {
	RawRtfParser rawParser = new RawRtfParser();
	StreamTextConverter tc = new StreamTextConverter();
	try (InputStream is = Test1.class.getResourceAsStream(RTF_SRC2); OutputStream os = System.out;) {
	    // os = new FileOutputStream(OUTFILE_NAME);
	    rawParser.parse(new RtfStreamSource(is), new MyListener());
	    // tc.convert(new RtfStreamSource(is), os, "UTF-8");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static class MyListener extends RtfListenerAdaptor {
	private static final int CMDS_ON_LINE = 6;
	private static final String CMD_FORMAT = "%16s";

	@Override
	public void processString(String string) {
	    string = string.trim();
	    if (!string.isEmpty()) {
		System.out.println(string + " ***************************************");
	    }
	}

	@Override
	public void processCharacterBytes(byte[] data) {
	    String text = new String(data).trim();
	    if (!text.isEmpty()) {
		if (cmdList.size() > 0) {
		    prntAndEmptyCmdList(cmdList);
		}
		System.out.println(new String(data));
	    }
	}

	@Override
	public void processDocumentEnd() {
	    if (cmdList.size() > 0) {
	    	prntAndEmptyCmdList(cmdList);
	    }
	}

	private void prntAndEmptyCmdList(List<String> cmdList) {
	    while (cmdList.size() > 0) {
		for (int i = 0; i < CMDS_ON_LINE; i++) {
		    String s = cmdList.size() > 0 ? cmdList.remove(0) : "";
		    System.out.printf(CMD_FORMAT, s);
		}
		System.out.println();
	    }
	}

	@Override
	public void processCommand(Command command, int parameter, boolean hasParameter, boolean optional) {

	    String name = command.getCommandName();
	    if (cmdIgnoreList.contains(name)) {
		return;
	    }
	    cmdList.add(name);
	    // System.out.println("Cmd: " + command);
	}
    }
}
