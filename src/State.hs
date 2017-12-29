{-# LANGUAGE DeriveGeneric #-}
{-# LANGUAGE OverloadedStrings #-}

module State (State(..), getSelectedFeed, articleCursorPositionIsValid, feedCursorPositionIsValid, noFeedSelected) where

import Data.Aeson
import GHC.Generics
import Feed
import Cursor
import Data.Maybe

data State = State { feeds :: [Feed], cursorFeeds :: Cursor, cursorArticles :: Cursor} deriving (Show, Generic, Eq)

instance ToJSON State 

instance FromJSON State 

getSelectedFeed :: State -> Maybe Feed
getSelectedFeed s 
  | (noFeedSelected s) = Nothing
  | otherwise = Just ((feeds s) !! (position $ cursorFeeds s))

noFeedSelected s = (cursorFeeds s) == NothingSelected
articleCursorPositionIsValid s = noArticleSelected || ((articleCursorPositionIsNotTooHigh s) && (articleCursorPositionIsNotTooLow s))
  where
    noArticleSelected = (cursorArticles s) == NothingSelected
    articleCursorPositionIsNotTooLow s = (position $ cursorArticles s) >= 0
    articleCursorPositionIsNotTooHigh s =  (position $ cursorArticles s) < (length $ articles $ fromJust $ getSelectedFeed s)
feedCursorPositionIsValid s = ((feedCursorPositionIsNotTooHigh s) && (feedCursorPositionIsNotTooLow s)) || noFeedSelected s
  where 
    feedCursorPositionIsNotTooLow s = (position $ cursorFeeds s) >= 0
    feedCursorPositionIsNotTooHigh s = (position $ cursorFeeds s) < (length $ feeds s)
