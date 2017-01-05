/**
 * 格式校验 onkeyup="*_in_progress" onafterpaste="*_finally(/\D/g,'')"
 */
function validate_int(element) {
	var value = $(element).val();
	$(element).val(value.replace(/\D/g, ''));
}

function validate_double_in_progress(element) {
	var value = $(element).val();
	var reg = /^(\d)+(\.)?(\d)*$/;
	if (!reg.test(value)) {
		value = value.replace(/[^0-9\.]/g, '');
		if (!reg.test(value)) {
			value = value.replace(/\D/g, '');
		}
		$(element).val(value);
	}
}

function validate_double_finally(element) {
	var value = $(element).val();
	var reg = /^(\d)+\.$/;
	if (reg.test(value)) {
		value = value + '00';
		$(element).val(value);
	}
	reg = /^(\d)+\.\d{1}$/;
	if (reg.test(value)) {
		value = value + '0';
		$(element).val(value);
	}
	reg = /^(\d)+\.\d{3,}$/;
	if (reg.test(value)) {
		var index = value.indexOf(".");
		var base = Number(value.substring(0, index + 3));
		var check = Number(value.substring(index + 3, index + 4)) >= 5 ? 0.01
				: 0;
		value = Number(base + check).toFixed(2);
		$(element).val(value);
	}
}

/**
 * @param num
 *            小数
 * @param decimal_digits
 *            小数位数
 */
function round(num, decimal_digits) {
	var index = num.indexOf(".");
	var offset = Math.pow(10, 0 - decimal_digits);
	var base = Number(num.substring(0, index + decimal_digits + 1));
	var check = Number(num.substring(index + decimal_digits + 1, index
			+ decimal_digits + 2)) >= 5 ? offset : 0;
	var value = Number(base + check).toFixed(decimal_digits);
	return value;
}