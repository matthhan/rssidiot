{-# LANGUAGE DeriveGeneric #-}
{-# LANGUAGE OverloadedStrings #-}
module Article (Article(..)) where
import Data.Aeson
import GHC.Generics

data Article = Article { title :: String, url::String, read::Bool } deriving (Show, Generic, Eq)


instance ToJSON Article 

instance FromJSON Article 
