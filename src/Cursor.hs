{-# LANGUAGE DeriveGeneric #-}
{-# LANGUAGE OverloadedStrings #-}
module Cursor (Cursor(..)) where
import Data.Aeson
import GHC.Generics

data Cursor = Cursor { position :: Int } | NothingSelected deriving (Show, Generic, Eq)


instance ToJSON Cursor 

instance FromJSON Cursor 
