module Feed (Feed(..)) where

data Feed = Feed { displayName :: String, url :: String, articles :: [Article] } deriving (Show)
