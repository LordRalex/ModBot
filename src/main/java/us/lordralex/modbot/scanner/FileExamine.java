package us.lordralex.modbot.scanner;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.pircbotx.Colors;
import us.lordralex.modbot.Main;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class FileExamine {

    private static final File DL_PATH;

    static {
        DL_PATH = new File("mnmbot-temp-files");
    }

    public FileExamine() {
    }

    public void start(String link) {
        DL_PATH.mkdirs();
        List<String> temp = new ArrayList<>();
        FileExamine1 ex = new FileExamine1(link, temp);
        ex.start();
    }

    private static class FileExamine1 extends Thread {

        private String link;
        private int id;
        private String[] downloadLinks;

        public FileExamine1(String threadLink, List<String> message) {
            link = threadLink;
            for (String test : link.split("/", 6)) {
                System.out.println(test);
            }
            id = Integer.parseInt(link.split("/", 6)[4].replace("-", ""));
            List<String> temp = new ArrayList<>();
            for (String line : message) {
                if (line.contains("(")) {
                    try {
                        line = line.substring(line.indexOf("(") + 1, line.indexOf(")") - 1);
                        temp.add(line);
                    } catch (StringIndexOutOfBoundsException e) {
                    }
                }
            }
            for (String a : temp) {
                System.out.println(a);
            }
            downloadLinks = message.toArray(new String[0]);
        }

        @Override
        public void run() {
            for (String aLink : downloadLinks) {
                try {
                    Extractor ex = new Extractor(aLink, id);
                    if (ex.checkForDat()) {
                        Main.getIrc().getDriver().sendMessage(link, Colors.BOLD + Colors.RED + link + " was found to have a .dat in the download");
                        return;
                    }
                } catch (Exception e) {
                    //if (!(e instanceof MalformedURLException)) {
                    e.printStackTrace(System.out);
                    //}
                }
            }
        }
        private static final File DL_PATH;

        static {
            DL_PATH = new File("Temp/Test-Mod/");
        }

        private final class Extractor {

            File folder = null;

            public Extractor(String a, int id) throws MalformedURLException, IOException {
                URL dl = new URL(a);
                String newL = deAdfly(a);
                newL = getMUrl(newL);
                dl = new URL(newL);
                if (a.endsWith(".jpeg")
                        || a.endsWith(".png")
                        || a.endsWith(".gif")) {
                    return;
                }
                folder = new File(DL_PATH, Integer.toString(id));
                download(dl.toString(), new File(folder, Integer.toString(id)).getPath());
                extract(Long.toString(id), folder.getPath());
            }

            public void download(String path, String fileName) throws MalformedURLException, IOException {
                URL test = new URL(path);
                new File(fileName).getParentFile().mkdirs();
                HttpURLConnection httpcon = (HttpURLConnection) test.openConnection();
                httpcon.addRequestProperty("User-Agent", "Mozilla/5.0");
                ReadableByteChannel rbc = Channels.newChannel(httpcon.getInputStream());
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.getChannel().transferFrom(rbc, 0, 1 << 24);
            }

            public void extract(String fileName, String pathToFolder) {
                new File(pathToFolder).mkdirs();
                ZipFile zipFile = null;
                try {
                    Enumeration entries;

                    zipFile = new ZipFile(pathToFolder + File.separator + fileName);
                    entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry) entries.nextElement();
                        if (entry.getName().contains("/")) {
                            (new File(pathToFolder, entry.getName().split("/")[0])).mkdir();
                        }
                        if (entry.isDirectory()) {
                            new File(pathToFolder, entry.getName()).mkdirs();
                            continue;
                        }

                        copyInputStream(zipFile.getInputStream(entry),
                                new BufferedOutputStream(new FileOutputStream(pathToFolder + File.separator + entry.getName())));
                        File extractedFile = new File(pathToFolder + File.separator + entry.getName());
                        if (extractedFile.getName().endsWith(".jar") || extractedFile.getName().endsWith(".zip")) {
                            extract(extractedFile.getName(), pathToFolder + File.separator + extractedFile.getName().substring(0, entry.getName().lastIndexOf("/")));
                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace(System.out);
                } finally {
                    if (zipFile != null) {
                        try {
                            zipFile.close();
                        } catch (IOException ex) {
                            ex.printStackTrace(System.out);
                        }
                    }
                }
            }

            private void copyInputStream(InputStream in, OutputStream out) {
                try {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) >= 0) {
                        out.write(buffer, 0, len);
                    }
                    in.close();
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace(System.out);
                }
            }

            public boolean checkForDat() {
                return check(folder, ".dat");
            }

            private boolean check(File aFile, String name) {
                if (aFile == null) {
                    return false;
                }
                File[] files = aFile.listFiles();
                if (files == null) {
                    return false;
                }
                for (File file : files) {
                    if (file == null) {
                        continue;
                    }
                    if (file.isDirectory()) {
                        check(file, name);
                    }
                    if (file.getName().endsWith(name)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    private static String getMUrl(String mediafire) {
        BufferedReader reader = null;
        try {
            URL path2 = new URL(mediafire);
            reader = new BufferedReader(new InputStreamReader(path2.openStream()));
            List<String> parts2 = new ArrayList<>();
            String s;
            while ((s = reader.readLine()) != null) {
                parts2.add(s);
            }
            reader.close();
            List<String> d = new ArrayList<>();
            for (String part : parts2) {
                String[] e = part.split(",");
                d.addAll(Arrays.asList(e));
            }
            for (int i = 0; i < d.size(); i++) {
                d.add(i, d.remove(i).trim());
                if (d.get(i).equalsIgnoreCase("")) {
                    d.remove(i);
                    i--;
                }
            }
            for (int i = 0; i < d.size(); i++) {
                String test = d.get(i);
                if (test.startsWith("kNO = ")) {
                    test = test.replace("kNO = ", "").replace("\"", "").trim();
                    return test;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return mediafire;
    }

    private static String deAdfly(String u) throws MalformedURLException, IOException {
        String end = u;
        URL url = new URL(u);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        List<String> parts = new ArrayList<>();
        String s;
        while ((s = reader.readLine()) != null) {
            parts.add(s);
        }
        List<String> b = new ArrayList<>();
        for (String part : parts) {
            String[] c = part.split(",");
            b.addAll(Arrays.asList(c));
        }
        for (String string : b) {
            string = string.trim();
            if (string.startsWith("var url")) {
                end = string.replace("var url =", "");
                end = end.replace("\'", "");
                end = end.replace(";", "");
                end = end.trim();
                if (!end.startsWith("https://adf.ly/")) {
                    end = "https://adf.ly/" + end;
                }
                break;
            }
        }

        URL newD = new URL(end);
        HttpURLConnection newC = (HttpURLConnection) newD.openConnection();
        DataInputStream input = new DataInputStream(newC.getInputStream());
        List<String> lines = new ArrayList<>();
        try {
            String line;
            while ((line = input.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (EOFException e) {
            reader.close();
        }

        String result = newD.toExternalForm();

        for (String line : lines) {
            System.out.println(line);
            if (line.startsWith("<META")) {
                result = line.split("URL=")[1].replace("\"", "").replace(">", "");
            }
        }
        return result;
    }
}
