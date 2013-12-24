(ns noir.response
  "Simple response helpers to change the content type, redirect, or return a canned response"
  (:use [noir.request :only [*request*]]
        [noir.util])
  (:refer-clojure :exclude [empty]))

(defn- ->map [c]
  (if-not (map? c)
    {:body c}
    c))

(defn set-headers
  "Add a map of headers to the given response. Headers must have
  string keys:

  (set-headers {\"x-csrf\" csrf}
    (common/layout [:p \"hey\"]))"
  [headers content]
  (update-in (->map content) [:headers ] merge headers))

(defn content-type
  "Wraps the response with the given content type and sets the body to the content."
  [ctype content]
  (set-headers {"Content-Type" ctype} content))

(defn xml
  "Wraps the response with the content type for xml and sets the body to the content."
  [content]
  (content-type "text/xml; charset=utf-8" content))

(let [generate-string (try-intern 'cheshire.core 'generate-string)
      generate-string*
      (fn [content]
        (if generate-string
          (generate-string content)
          (errln "Do nothing."
            "You can add `[cheshire \"5.3.0\"]` to your :dependencies.")))]
  (defn json
    "Wraps the response in the json content type and generates JSON from the content.

    Add `[cheshire \"5.3.0\"]` to your :dependencies."
    [content]
    (content-type "application/json; charset=utf-8"
      (generate-string* content)))

  (defn jsonp
    "Generates JSON for the given content and creates a javascript response for calling
    func-name with it.

    Add `[cheshire \"5.3.0\"]` to your :dependencies."
    [func-name content]
    (content-type "application/json; charset=utf-8"
      (str func-name "(" (generate-string* content) ");"))))

(defn status
  "Wraps the content in the given status code"
  [code content]
  (assoc (->map content) :status code))

(defn redirect
  "A header redirect to a different URI. If given one argument,
   returns a 302 Found redirect. If given two arguments, the
   second argument should be a keyword indicating which redirect
   status to use. Choices are:

   :permanent    -- A 301 permanent redirect.
   :found        -- A 302 found redirect (default).
   :see-other    -- A 303 see other redirect.
   :not-modified -- A 304 not modified redirect.
   :proxy        -- A 305 proxy redirect.
   :temporary    -- A 307 temporary redirect.

   To see what these redirects are for in detail, visit
   http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3

   finally, request can be passed in as the third argument, useful
   if request is bound to a var"
  ([url] (redirect url :found *request*))
  ([url type] (redirect url type *request*))
  ([url type request]
    (let [context (:context request)
          url     (if (and context (not (.contains url "://")))
                    (str context url) url)]
      {:status (case type
                 :permanent 301
                 :found 302
                 :see-other 303
                 :not-modified 304
                 :proxy 305
                 :temporary 307)
       :headers {"Location" url}
       :body ""})))

(defn empty
  "Return a successful, but completely empty response"
  []
  {:status 200
   :body ""})

(defn edn
  "Wraps the response in the `application/edn` content-type
   and calls pr-str on the Clojure data stuctures passed in."
  [data]
  (content-type "application/edn; charset=utf-8"
                (pr-str data)))
