module Article (Article(..)) where

data Article = Article { title :: String, url::String, read::Bool } deriving (Show)


