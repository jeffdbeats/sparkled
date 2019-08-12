import { useEffect } from "react";

const useTitle = (title: string) => {
  useEffect(() => {
    document.title = `Sparkled | ${title}`;
  }, [title]);
};

export default useTitle;
