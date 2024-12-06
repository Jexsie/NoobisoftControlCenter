export type User = {
  email: string;
  accountId: string;
};

export type NftMeta = {
  name: string;
  description: string;
  image: string;
  type: string;
  properties: {
      game: string;
  },
  files: [
    {
      uri:string,
      type: string,
      is_default_file: boolean
    }
  ],
  localization: {
    uri: string;
    default: string;
    locales: Array<string>
  }
}
