
pub trait CanReplaceAtIndex<T> {
    fn replace_at_index(&self, index:usize, f: &Fn(Option<T>) -> T) -> Self;
}
impl<T> CanReplaceAtIndex<T> for Vec<T> where T:Clone {
    fn replace_at_index(&self,ind:usize,f:&Fn(Option<T>) -> T) -> Vec<T> {
        let mut newvec = self.to_vec(); 
        if ind >= newvec.len() {
            newvec.push(f(None))
        } else {
            newvec[ind] = f(Some(newvec[ind].clone()))
        }
        newvec
    }
}
