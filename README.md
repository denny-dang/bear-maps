# BearMaps

BearMaps is a web mapping application of Berkeley inspired by the OpenStreetMap project. I developed the backend logic for rasterizing map images, finding the most optimal path between two locations, and autocompleting searches.


Feature | Description
------- | -------
[AStarSolver](https://github.com/denny-dang/bear-maps/blob/master/bearmaps/utils/graph/AStarSolver.java) | The A* search algorithm leveraged to find the shortest path between two location points in Berkeley.
[AugmentedStreetMapGraph](https://github.com/denny-dang/bear-maps/blob/master/bearmaps/AugmentedStreetMapGraph.java) | Augmented graph representation of Berkeley Open Street Map data.
[KDTree](https://github.com/denny-dang/bear-maps/blob/master/bearmaps/utils/ps/KDTree.java) | A K-Dimensional Tree utilized in the the A* search algorithm, allowing efficient nearest neighbor lookup averaging O(log(n)) time, where n is the number of nodes in the tree.
[MyTrieSet](https://github.com/denny-dang/bear-maps/blob/master/bearmaps/utils/ps/MyTrieSet.java) | A TrieSet utilized in the search autocomplete functionality, matching a prefix to valid location names in Θ(k) time, where k being the number of words sharing the prefix.
[RasterAPIHandler](https://github.com/denny-dang/bear-maps/blob/master/bearmaps/server/handler/impl/RasterAPIHandler.java) | API Handler that renders map images provided a user's requested area and level of zoom.
