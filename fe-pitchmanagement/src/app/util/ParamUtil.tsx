export class ParamUtil {
    public static toRequestParams<T>(obj: any) {
        const params: RequestParam[] = [];
        Object.keys(obj).forEach((k) => {
            if (obj[k] !== undefined) {
                params.push(new RequestParam(k, obj[k]));
            }
        });
        return params;
    }

    public static toRequestParamsByValueAndKey<T>(values: [], key: string) {
        const params: RequestParam[] = [];
        values.forEach((val) => {
            params.push(new RequestParam(key, val));
        });
        return params;
    }
}

export class RequestParam {
    name: string;
    value: string;

    constructor(name: string, value: string) {
        this.name = name;
        this.value = value;
    }

}