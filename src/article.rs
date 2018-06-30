
#[derive(Debug,Clone,Serialize,Deserialize,Eq,PartialEq)]
pub struct Article {
    title: String,
    url: String,
    read: bool,
}
impl Article {
    pub fn set_read(self) -> Article {
        Article {
            read: true,
            ..self
        }
    }
    pub fn new(title: String, url: String) -> Article {
        Article {
            title,
            url,
            read: false,
        }
    }
}

#[cfg(test)]
mod article_tests {
    use serde_json;
    use article::Article;
    #[test]
    fn setting_read_works() {
        let art = Article::new(String::from("ayy"),String::from("lmao"));
        assert!(!art.read);
        let art = art.set_read();
        assert!(art.read);
        let art = art.set_read();
        assert!(art.read);
    }
    #[test]
    fn can_serialize_and_deserialize() {
        let art = Article::new(String::from("ayy"),String::from("lmao"));
        let art2 = serde_json::from_str(&serde_json::to_string(&art).unwrap()).unwrap();
        assert_eq!(art,art2)
    }
}
