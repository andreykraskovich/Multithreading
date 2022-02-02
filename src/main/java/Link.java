import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveTask;

public class Link  extends RecursiveTask<String> {

    private String url;
    private static String startUrl;
    private static CopyOnWriteArraySet<String> allLinks = new CopyOnWriteArraySet<>();

    public Link(String url) {
        this.url = url.trim();
    }

    public Link(String url, String startUrl) {
        this.url = url.trim();
        Link.startUrl = startUrl.trim();
    }


    @Override
    protected String compute() {
        StringBuffer sb = new StringBuffer(url + "\n");
        Set<Link> subTask = new HashSet<>();

        getChildren(subTask);

        for (Link link : subTask) {
            sb.append(link.join());
        }
        return sb.toString();
    }

    private void getChildren(Set<Link> subTask) {
        Document doc;
        Elements elements;
        try {
            Thread.sleep(200);
            doc = Jsoup.connect(url).get();
            elements = doc.select("a");
            for (Element el : elements) {
                String attr = el.attr("abs:href");
                if (!attr.isEmpty() && attr.startsWith(startUrl) && !allLinks.contains(attr) && !attr
                        .contains("#")) {
                    Link link = new Link(attr);
                    link.fork();
                    subTask.add(link);
                    allLinks.add(attr);
                }
            }
        } catch (InterruptedException | IOException ignored) {
        }
    }
}
