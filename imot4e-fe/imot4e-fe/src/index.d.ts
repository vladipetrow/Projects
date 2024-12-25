// src/global.d.ts or src/google-maps.d.ts
export {}; // Make this file a module

declare global {
  namespace google {
    export namespace maps {
      // Add additional custom definitions for google.maps here if necessary
      // For example:
      interface Map {}
      interface Marker {}
    }
  }
}

declare module "*.html" {
  const value: string;
  export default value;
}
