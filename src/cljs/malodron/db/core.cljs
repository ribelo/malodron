(ns malodron.db.core
  (:require [re-frame.core :as rf]))


(defn save-local-storage [db]
  (.setItem js/localStorage "malodron" db))


(def ->local-storage (rf/after save-local-storage))


(defn load-local-storage []
  (some->> (.getItem js/localStorage "malodron")
           (cljs.reader/read-string)))


(def default-db
  {:view/active-panel :store/salesroom})
