declare module "react-google-maps" {
  export var GoogleMap: GoogleMap;
}

interface GoogleMap {
    (any): any;
}

declare module '*.html' {
  const value: string;
  export default value
}