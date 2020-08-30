package bearmaps;

import bearmaps.utils.graph.streetmap.Node;
import bearmaps.utils.graph.streetmap.StreetMapGraph;
import bearmaps.utils.ps.KDTree;
import bearmaps.utils.ps.MyTrieSet;
import bearmaps.utils.ps.Point;
import bearmaps.utils.ps.WeirdPointSet;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    KDTree tree;
    HashMap<Point, Node> pointMap;
    HashMap<String, LinkedList<Node>> locationNames;
    MyTrieSet nameTrie;
    HashMap<String, LinkedList<String>> fullNamesMap;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        List<Node> nodes = this.getNodes();
        pointMap = new HashMap<>();
        nameTrie = new MyTrieSet();
        locationNames = new HashMap<>();
        fullNamesMap = new HashMap<>();
        for (Node n : nodes) {
            if (!neighbors(n.id()).isEmpty()) {
                pointMap.put(new Point(n.lon(), n.lat()), n);
            }
            if (n.name() != null) {
                String cleanName = cleanString(n.name());
                nameTrie.add(cleanName);
                if (!fullNamesMap.containsKey(cleanName)) {
                    fullNamesMap.put(cleanName, new LinkedList<>());
                }
                fullNamesMap.get(cleanName).add(n.name());
                if (!locationNames.containsKey(cleanName)) {
                    locationNames.put(cleanName, new LinkedList<>());
                }
                locationNames.get(cleanName).add(n);
            }
        }
        List<Point> points = new ArrayList<>(pointMap.keySet());
        tree = new KDTree(points);
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * 
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point nearPoint = tree.nearest(lon, lat);
        return pointMap.get(nearPoint).id();
    }

    /**
     * In linear time, collect all the names of OSM locations that prefix-match the
     * query string.
     * 
     * @param prefix Prefix string to be searched for. Could be any case, with our
     *               without punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name
     *         matches the cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        String cleanedPrefix = cleanString(prefix);
        List<String> outputNames = new LinkedList<>();
        for (String cleanName : nameTrie.keysWithPrefix(cleanedPrefix)) {
            for (String fullName : fullNamesMap.get(cleanName)) {
                outputNames.add(fullName);
            }
        }
        return outputNames;
    }

    /**
     * Collect all locations that match a cleaned <code>locationName</code>, and
     * return information about each node that matches.
     * 
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the cleaned
     *         <code>locationName</code>, and each location is a map of parameters
     *         for the Json response as specified: <br>
     *         "lat" -> Number, The latitude of the node. <br>
     *         "lon" -> Number, The longitude of the node. <br>
     *         "name" -> String, The actual name of the node. <br>
     *         "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        LinkedList<Node> nodeList = locationNames.get(cleanString(locationName));
        LinkedList<Map<String, Object>> output = new LinkedList<>();
        HashMap<String, Object> curr;
        for (Node node : nodeList) {
            curr = new HashMap<>();
            curr.put("lat", node.lat());
            curr.put("lon", node.lon());
            curr.put("name", node.name());
            curr.put("id", node.id());
            output.add(curr);
        }
        return output;
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and
     * capitalization.
     * 
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
