
package firstTest;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rtfparserkit.converter.text.StreamTextConverter;
import com.rtfparserkit.parser.RtfListenerAdaptor;
import com.rtfparserkit.parser.RtfStreamSource;
import com.rtfparserkit.parser.raw.RawRtfParser;
import com.rtfparserkit.rtf.Command;

public class Test2 {
    private static final String RTF_SRC2 = "data/TestFaceSheet.rtf";
    private static final String OUTFILE_NAME = "Test1Out.txt";
    // !! get rid of??
    // !! private static List<String> cmdList = new ArrayList<>(); // !!
    private static final boolean DATA_FROM_CLIPBOARD = true;

    private static Map<String, RtfState> cmdToStateMap = new HashMap<>();

    static {
        for (RtfState state : RtfState.values()) {
            cmdToStateMap.put(state.getCommand(), state);
        }
    }

    public static void main(String[] args) {
        RawRtfParser rawParser = new RawRtfParser();
        StreamTextConverter tc = new StreamTextConverter();
        try (
                InputStream is = DATA_FROM_CLIPBOARD ? 
                            getClipbrdDataIS() : 
                            Test2.class.getResourceAsStream(RTF_SRC2);
                OutputStream os = System.out;
        ) {
            // os = new FileOutputStream(OUTFILE_NAME);
            rawParser.parse(new RtfStreamSource(is), new MyListener());
            // tc.convert(new RtfStreamSource(is), os, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (UnsupportedFlavorException e1) {
            e1.printStackTrace();
        }
    }
    
    public static InputStream getClipbrdDataIS() throws ClassNotFoundException, UnsupportedFlavorException, IOException {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
       return (InputStream) clipboard.getData(new DataFlavor("text/rtf"));
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
                System.out.println(text);
            }
            // !! if (!text.isEmpty()) {
            // if (cmdList.size() > 0) {
            // prntAndEmptyCmdList(cmdList);
            // }
            // System.out.println(new String(data));
            // }
        }

        @Override
        public void processDocumentEnd() {
            // if (cmdList.size() > 0) {
            // prntAndEmptyCmdList(cmdList);
            // }
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
        public void processCommand(Command command, int parameter, boolean hasParameter,
                boolean optional) {

            String cmd = command.getCommandName();
            RtfState state = cmdToStateMap.get(cmd);
            if (state != null) {
                String display = String.format("\t%s", state.name());
                System.out.println(display);
                // !! cmdList.add(cmd);
            }
            // System.out.println("Cmd: " + command);
        }
    }
}
