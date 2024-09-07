export class TokenService {
    private static _tokenService: TokenService;
  
    public static getInstance(): TokenService {
      if (!TokenService._tokenService) {
        TokenService._tokenService = new TokenService();
      }
      return TokenService._tokenService;
    }
  
    public setToken(token: string){
      localStorage.setItem("access_token", token);
    }
  
    public getToken(): any {
      return localStorage.getItem("access_token");
    }
  
    public removeToken() {
      localStorage.removeItem("access_token");
    }
  }