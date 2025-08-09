(ns app.search
  (:require [react-native :as rn]
            [uix.core :refer [$ defui] :as uix]
            [app.data :refer [mock-posts users]]
            [components.feed-item :refer [feed-item]]))

(def trending-topics
  [#:trending{:tag "#ClojureScript"
              :posts 1247
              :category "Technology"}
   #:trending{:tag "#ReactNative"
              :posts 892
              :category "Development"}
   #:trending{:tag "#FunctionalProgramming"
              :posts 634
              :category "Programming"}
   #:trending{:tag "#MobileDev"
              :posts 567
              :category "Technology"}
   #:trending{:tag "#OpenSource"
              :posts 445
              :category "Technology"}])

(def recent-searches
  ["ClojureScript hot reload"
   "React Native navigation"
   "@sarahc"
   "functional programming"
   "#MobileDev"])

(defn search-posts [query posts]
  "Search posts by content"
  (if (empty? query)
    []
    (->> posts
         (filter #(or (clojure.string/includes?
                       (clojure.string/lower-case (:content %))
                       (clojure.string/lower-case query))
                      (clojure.string/includes?
                       (clojure.string/lower-case (get-in % [:user :name]))
                       (clojure.string/lower-case query))
                      (clojure.string/includes?
                       (clojure.string/lower-case (get-in % [:user :username]))
                       (clojure.string/lower-case query))))
         (take 20))))

(defn search-users [query all-users]
  "Search users by name or username"
  (if (empty? query)
    []
    (->> all-users
         (vals)
         (filter #(or (clojure.string/includes?
                       (clojure.string/lower-case (:name %))
                       (clojure.string/lower-case query))
                      (clojure.string/includes?
                       (clojure.string/lower-case (:username %))
                       (clojure.string/lower-case query))))
         (take 10))))

(defui search-header [{:keys [on-back search-query on-search-change on-clear-search]}]
  "Search screen header with input"
  ($ rn/SafeAreaView {:style {:background-color "white"
                              :border-bottom-width 0.5
                              :border-bottom-color "#e1e8ed"}}
     ($ rn/View {:style {:flex-direction "row"
                         :align-items "center"
                         :padding-horizontal 16
                         :padding-vertical 12}}

        ;; Back button
        ($ rn/TouchableOpacity {:on-press on-back
                                :style {:padding 8
                                        :margin-right 8}}
           ($ rn/Text {:style {:font-size 18}} "←"))

        ;; Search input
        ($ rn/View {:style {:flex 1
                            :background-color "#f7f9fa"
                            :border-radius 20
                            :flex-direction "row"
                            :align-items "center"
                            :padding-horizontal 12}}
           ($ rn/Text {:style {:font-size 16
                               :color "#536471"
                               :margin-right 8}} "🔍")
           ($ rn/TextInput {:style {:flex 1
                                    :font-size 16
                                    :color "#0f1419"
                                    :padding-vertical 10}
                            :placeholder "Search posts and users..."
                            :placeholder-text-color "#536471"
                            :value search-query
                            :on-change-text on-search-change
                            :auto-focus true
                            :return-key-type "search"})
           (when (not (empty? search-query))
             ($ rn/TouchableOpacity {:on-press on-clear-search
                                     :style {:padding 4}}
                ($ rn/Text {:style {:font-size 14
                                    :color "#536471"}} "✕")))))))

(defui trending-item [{:keys [trending on-press]}]
  "Individual trending topic item"
  ($ rn/TouchableOpacity {:style {:padding 12
                                  :border-bottom-width 0.5
                                  :border-bottom-color "#f7f9fa"}
                          :on-press #(when on-press
                                       (on-press (:trending/tag trending)))}
     ($ rn/View {:style {:flex-direction "row"
                         :justify-content "space-between"
                         :align-items "center"}}
        ($ rn/View {:style {:flex 1}}
           ($ rn/Text {:style {:font-size 13
                               :color "#536471"
                               :margin-bottom 2}}
              (str "Trending in " (:trending/category trending)))
           ($ rn/Text {:style {:font-size 16
                               :font-weight "bold"
                               :color "#0f1419"
                               :margin-bottom 2}}
              (:trending/tag trending))
           ($ rn/Text {:style {:font-size 13
                               :color "#536471"}}
              (str (:trending/posts trending) " posts")))
        ($ rn/Text {:style {:font-size 16
                            :color "#536471"}} "→"))))

(defui user-result-item [{:keys [user on-press]}]
  "User search result item"
  ($ rn/TouchableOpacity {:style {:flex-direction "row"
                                  :align-items "center"
                                  :padding 12
                                  :border-bottom-width 0.5
                                  :border-bottom-color "#f7f9fa"}
                          :on-press #(when on-press
                                       (on-press (:username user)))}
     ($ rn/View {:style {:width 40
                         :height 40
                         :border-radius 20
                         :background-color "#e1e8ed"
                         :justify-content "center"
                         :align-items "center"
                         :margin-right 12}}
        ($ rn/Text {:style {:font-size 16}} "👤"))
     ($ rn/View {:style {:flex 1}}
        ($ rn/Text {:style {:font-size 16
                            :font-weight "bold"
                            :color "#0f1419"}}
           (:name user))
        ($ rn/Text {:style {:font-size 14
                            :color "#536471"
                            :margin-top 2}}
           (:username user))
        (when (:bio user)
          ($ rn/Text {:style {:font-size 13
                              :color "#536471"
                              :margin-top 2
                              :line-height 16}
                      :number-of-lines 2}
             (:bio user))))))

(defui recent-search-item [{:keys [search on-press on-remove]}]
  "Recent search item"
  ($ rn/View {:style {:flex-direction "row"
                      :align-items "center"
                      :padding 12
                      :border-bottom-width 0.5
                      :border-bottom-color "#f7f9fa"}}
     ($ rn/TouchableOpacity {:style {:flex 1
                                     :flex-direction "row"
                                     :align-items "center"}
                             :on-press #(when on-press
                                          (on-press search))}
        ($ rn/Text {:style {:font-size 16
                            :color "#536471"
                            :margin-right 12}} "🕐")
        ($ rn/Text {:style {:font-size 16
                            :color "#0f1419"}}
           search))
     ($ rn/TouchableOpacity {:style {:padding 8}
                             :on-press #(when on-remove
                                          (on-remove search))}
        ($ rn/Text {:style {:font-size 14
                            :color "#536471"}} "✕"))))

(defui search-results [{:keys [search-query post-results user-results on-profile-click on-thread-click on-like on-reply on-repost]}]
  "Search results display"
  ($ rn/View {:style {:flex 1}}
     ($ rn/ScrollView {:style {:flex 1}}

        ;; Users section
        (when (not (empty? user-results))
          ($ rn/View {}
             ($ rn/View {:style {:padding 16
                                 :background-color "#f7f9fa"}}
                ($ rn/Text {:style {:font-size 18
                                    :font-weight "bold"
                                    :color "#0f1419"}}
                   "People"))
             (for [user user-results]
               ($ user-result-item {:key (:username user)
                                    :user user
                                    :on-press on-profile-click}))))

        ;; Posts section
        (when (not (empty? post-results))
          ($ rn/View {}
             ($ rn/View {:style {:padding 16
                                 :background-color "#f7f9fa"}}
                ($ rn/Text {:style {:font-size 18
                                    :font-weight "bold"
                                    :color "#0f1419"}}
                   "Posts"))
             (for [post post-results]
               ($ feed-item {:key (:id post)
                             :post post
                             :on-like on-like
                             :on-reply on-reply
                             :on-repost on-repost
                             :on-profile-click on-profile-click
                             :on-thread-click #(on-thread-click (:thread-id post))})))))))

(defui search-screen [{:keys [on-back on-profile-click on-thread-click]}]
  "Main search screen"
  (let [[search-query set-search-query!] (uix/use-state "")
        [search-history set-search-history!] (uix/use-state recent-searches)

        post-results (search-posts search-query mock-posts)
        user-results (search-users search-query users)
        has-results? (or (not (empty? post-results)) (not (empty? user-results)))

        handle-search-change (fn [text]
                               (set-search-query! text))

        handle-clear-search (fn []
                              (set-search-query! ""))

        handle-recent-search (fn [search]
                               (set-search-query! search))

        handle-trending-search (fn [tag]
                                 (set-search-query! tag))

        handle-remove-search (fn [search]
                               (set-search-history!
                                (fn [history]
                                  (vec (remove #(= % search) history)))))

        handle-like (uix/use-callback
                     (fn [post-id]
                       (js/console.log "Like post" post-id))
                     [])

        handle-reply (uix/use-callback
                      (fn [post-id]
                        (js/console.log "Reply to post" post-id))
                      [])

        handle-repost (uix/use-callback
                       (fn [post-id]
                         (js/console.log "Repost" post-id))
                       [])]

    ($ rn/View {:style {:flex 1
                        :background-color "#ffffff"}}

       ;; Header with search input
       ($ search-header {:on-back on-back
                         :search-query search-query
                         :on-search-change handle-search-change
                         :on-clear-search handle-clear-search})

       (if (not (empty? search-query))
         ;; Search results
         (if has-results?
           ($ search-results {:search-query search-query
                              :post-results post-results
                              :user-results user-results
                              :on-profile-click on-profile-click
                              :on-thread-click on-thread-click
                              :on-like handle-like
                              :on-reply handle-reply
                              :on-repost handle-repost})
           ;; No results
           ($ rn/View {:style {:flex 1
                               :justify-content "center"
                               :align-items "center"}}
              ($ rn/Text {:style {:font-size 18
                                  :color "#536471"}}
                 "No results found")
              ($ rn/Text {:style {:font-size 14
                                  :color "#536471"
                                  :margin-top 8}}
                 (str "Try searching for \"" search-query "\""))))

         ;; Default view (trending + recent searches)
         ($ rn/ScrollView {:style {:flex 1}}

            ;; Trending section
            ($ rn/View {:style {:margin-top 16}}
               ($ rn/View {:style {:padding 16
                                   :background-color "#f7f9fa"}}
                  ($ rn/Text {:style {:font-size 18
                                      :font-weight "bold"
                                      :color "#0f1419"}}
                     "Trending"))
               (for [trending trending-topics]
                 ($ trending-item {:key (:trending/tag trending)
                                   :trending trending
                                   :on-press handle-trending-search})))

            ;; Recent searches
            (when (not (empty? search-history))
              ($ rn/View {:style {:margin-top 24}}
                 ($ rn/View {:style {:padding 16
                                     :background-color "#f7f9fa"}}
                    ($ rn/Text {:style {:font-size 18
                                        :font-weight "bold"
                                        :color "#0f1419"}}
                       "Recent"))
                 (for [search search-history]
                   ($ recent-search-item {:key search
                                          :search search
                                          :on-press handle-recent-search
                                          :on-remove handle-remove-search})))))))))