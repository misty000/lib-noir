(ns noir.util)

(defn try-intern
  [^clojure.lang.Symbol ns ^clojure.lang.Symbol name]
  (try
    (require ns)
    (var-get (intern ns name))
    (catch Exception e)))

(defn errln [& more]
  (binding [*out* *err*]
    (apply println more)))