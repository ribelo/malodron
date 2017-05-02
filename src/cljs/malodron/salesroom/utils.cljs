(ns malodron.salesroom.utils)


(defn shelves->grid [[rows cols] shelves]
  (let [flat-grid (vec (repeat rows (vec (repeat cols 0))))]
    (reduce (fn [grid shelf]
              (assoc-in grid shelf 1))
            flat-grid
            shelves)))


(defn neighbours-coords
  [[row col] grid]
  (let [rows (count grid)
        cols (count (first grid))
        indices [[1 0] [0 1] [-1 0] [0 -1]]]
    (reduce (fn [coords [i j]]
              (let [x (+ row i)
                    y (+ col j)]
                (if (and (< -1 x rows) (< -1 y cols))
                  (conj coords [x y])
                  coords)))
            []
            indices)))


(defn sweep-cell
  ([[row col] grid]
   (sweep-cell [row col] grid #{}))
  ([[row col] grid region]
   (let [neighbours (neighbours-coords [row col] grid)]
     (if (pos? (get-in grid [row col]))
       (let [region (conj region [row col])]
         (reduce (fn [region [nx ny]]
                   (if (not (contains? region [nx ny]))
                     (clojure.set/union region (sweep-cell [nx ny] grid region))
                     region))
                 region
                 neighbours))
       region))))


(defn shelves->racks [size shelves]
  (let [grid (shelves->grid size shelves)]
    (loop [[cord & cords] shelves racks #{}]
      (if cord
        (recur cords (conj racks (sweep-cell cord grid)))
        (->> racks (map vec) (vec) (sort-by first) (vec))))))


(defn int->char [num]
  (.fromCharCode js/String (+ 97 num)))



(char-range \a \f)

(.charCodeAt "z" 0)

(.fromCharCode js/String 97)