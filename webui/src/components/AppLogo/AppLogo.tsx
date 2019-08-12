import React from "react";
import largeLogo from "./logo-large.svg";

interface Props extends React.HTMLAttributes<HTMLElement> {}

const AppLogo: React.FC<Props> = (props: Props) => {
  return <img className={props.className} src={largeLogo} alt="Sparkled"/>;
};

export default AppLogo;
