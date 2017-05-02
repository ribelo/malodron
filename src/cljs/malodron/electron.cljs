(ns malodron.electron)

(def electron (js/require "electron"))
(def remote (.-remote electron))
(def app (.-app remote))
(def browser-window (.-BrowserWindow remote))
;(def shell (.-shell remote))
(def fs (.require remote "fs"))
