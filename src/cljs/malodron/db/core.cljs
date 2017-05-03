(ns malodron.db.core
  (:require [re-frame.core :as rf]))


(defn save-local-storage [db]
  (.setItem js/localStorage "malodron" db))


(def ->local-storage (rf/after save-local-storage))


(defn load-local-storage []
  (some->> (.getItem js/localStorage "malodron")
           (cljs.reader/read-string)))

(.setItem js/localStorage "malodron" {:product/item :a})
(.getItem js/localStorage "malodron")
(load-local-storage)
(clj->js {:product/item :a})

(def default-db
  {:view/active-panel :store/salesroom})
