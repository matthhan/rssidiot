use article::Article;
use circular_buffer::CircularBuffer;
use can_replace_at_index::CanReplaceAtIndex;




#[derive(Debug,Serialize,Deserialize,PartialEq,Eq,Clone)]
pub struct Feed {
    name: String,
    url: String,
    articles: CircularBuffer<Article>,
}
impl Feed {
    pub fn new(name:String,url:String) -> Feed {
        let articles: CircularBuffer<Article> = CircularBuffer::new(100);
        Feed {
            name,
            url,
            articles,
        }
    }
    pub fn set_article_read(self,article_ind:usize) -> Feed {
        Feed { articles: self.articles.replace_at_index(article_ind, &|o_article: Option<Article>| -> Article { o_article.unwrap().set_read() } ), ..self}
    }
    pub fn len(&self) -> usize {
        self.articles.len()
    }
}
#[cfg(test)]
mod feed_tests {
    use feed::Feed;
    extern crate serde_json;
    #[test]
    fn can_serialize_and_deserialize() {
        let fe = Feed::new("Example Feed".to_string(), "www.example.com".to_string());
        let as_str = serde_json::to_string(&fe).unwrap(); 
        let from_str = serde_json::from_str(&as_str).unwrap();
        assert_eq!(fe,from_str)
    }
}
