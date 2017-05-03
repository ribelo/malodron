(ns malodron.db.core
  (:require [re-frame.core :as rf]
            [cognitect.transit :as t]))


(defn save-local-storage [db]
  (let [w (t/writer :json)]
    (.setItem js/localStorage "malodron" (t/write w db))))


(def ->local-storage (rf/after save-local-storage))


(defn load-local-storage []
  (let [r (t/reader :json)]
    (some->> (.getItem js/localStorage "malodron")
             (t/read r))))


(def default-db
  {:view/active-panel :store/salesroom})
