import * as ns from "jquery"

class StringUtils {
	static false: string = "";

	static　toString(value: object): string {
		return JSON.stringify(value.toString);
	}

	static　isEmpty(value: String): boolean {
		return value == null || value == undefined || (this.instanceOf(value) && !value.length);
	}
	
	static　isNotEmpty(value: string): boolean {
		return !this.isEmpty(value);
	}

	static　instanceOf(obj: object): boolean {
		return obj != null && obj != undefined && obj.constructor == String;
	}
};

export {StringUtils};
