(ns malodron.views.core
  (:require [re-frame.core :as rf]
            [semantic-ui.core :as ui]
            [malodron.salesroom.ui :as salesroom]
            [malodron.flex :as flex]))

;(defn main-panel []
;  (fn []
;    [ui/grid {:columns   1
;              :padded    true
;              :container true
;              :style     {:min-width "100%"}}
;     [ui/grid-column
;      [:div "sex"]]]))

(defn main-panel []
  (fn []
    [flex/hbox {:title "bla"
                :style {:background-color "red"
                        :min-height "100px"
                        :max-width "100px"}}
     [:div "sex"]
     [:div "sex1"]]))