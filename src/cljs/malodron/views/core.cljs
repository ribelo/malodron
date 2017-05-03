(ns malodron.views.core
  (:require [re-frame.core :as rf]
            [semantic-ui.core :as ui]
            [malodron.salesroom.ui :as salesroom]))

(defn main-panel []
  (fn []
    [ui/grid {:columns   1
              :padded    true
              :container true
              :style     {:min-width "100%"}}
     [ui/grid-column
      [salesroom/component]]]))