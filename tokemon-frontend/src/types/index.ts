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
  };
  files: [
    {
      uri: string;
      type: string;
      is_default_file: boolean;
    }
  ];
  localization: {
    uri: string;
    default: string;
    locales: Array<string>;
  };
};

export type AccountMeta = {
  nfts: [
    {
      account_id: string;
      created_timestamp: string;
      delegating_spender: string | null;
      deleted: false;
      metadata: string;
      modified_timestamp: string;
      serial_number: number;
      spender: string | null;
      token_id: string;
    }
  ];
  links: {
    next: string;
  };
};
