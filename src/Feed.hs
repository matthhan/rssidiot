{-# LANGUAGE DeriveGeneric #-}
{-# LANGUAGE OverloadedStrings #-}
module Feed (Feed(..)) where

import Data.Aeson
import GHC.Generics
import Article

data Feed = Feed { displayName :: String, url :: String, articles :: [Article] } deriving (Show, Generic, Eq)

instance ToJSON Feed 

instance FromJSON Feed 
