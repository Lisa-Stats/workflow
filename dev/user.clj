(ns user
  (:require
   [integrant.repl :as ig-repl]
   [workflow.main :as wf-main])) ;; our server

(ig-repl/set-prep! (fn []
                     wf-main/config)) ;;returning the config def defined in
;;workflow.main

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)
