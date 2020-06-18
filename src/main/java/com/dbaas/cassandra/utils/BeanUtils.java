package com.dbaas.cassandra.utils;

import static com.dbaas.cassandra.utils.StringUtils.isBlank;
import static org.springframework.beans.BeanUtils.getPropertyDescriptor;
import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * BeanUtils
 *
 */
@Component("BeanUtils")
public final class BeanUtils {

	/**
	 * 内部コンストラクタ
	 */
	private BeanUtils() {

	}

	/**
	 * 型違いに対応したコピーメソッド
	 *
	 * @param source コピー元
	 * @param target コピー先
	 * @throws BeansException BeansException
	 */
	public static void copyCrossTypeProperties(Object source, Object target) throws BeansException {
		copyCrossTypeProperties(source, target, null, (String[]) null);
	}

	/**
	 * 型違いに対応したコピーメソッド(無視フィールド指定可能版)
	 *
	 * @param source コピー元
	 * @param target コピー先
	 * @param ignoreProperties コピー対象外のプロパティ名配列
	 * @throws BeansException BeansException
	 */
	public static void copyCrossTypeProperties(Object source, Object target, @Nullable String... ignoreProperties)
			throws BeansException {
		copyCrossTypeProperties(source, target, null, ignoreProperties);
	}

	/**
	 * 型違いに対応したコピーメソッドのメイン処理<br>
	 * {@link BeanUtils#copyProperties}の改造版
	 *
	 * @param source the source bean
	 * @param target the target bean
	 * @param editable the class (or interface) to restrict property setting to
	 * @param ignoreProperties array of property names to ignore
	 * @throws BeansException if the copying failed
	 * @see BeanWrapper
	 */
	private static void copyCrossTypeProperties(Object source, Object target, @Nullable Class< ? > editable,
			@Nullable String... ignoreProperties) throws BeansException {

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class< ? > actualEditable = target.getClass();
		if (editable != null) {
			if (!editable.isInstance(target)) {
				throw new IllegalArgumentException("Target class [" + target.getClass().getName()
						+ "] not assignable to Editable class [" + editable.getName() + "]");
			}
			actualEditable = editable;
		}
		// コピー先のPropertyDescriptorの生成
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

		for (PropertyDescriptor targetPd : targetPds) {
			// コピー先の書き込みメソッドが取得できない もしくは 無視リストのプロパティ名と一致する際は次の項目へ
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod == null || (ignoreList != null && ignoreList.contains(targetPd.getName()))) {
				continue;
			}
			// コピー元のPropertyDescriptorの生成
			PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
			if (sourcePd == null) {
				continue;
			}
			// コピー元の読込メソッドが取得できない場合は次の項目へ
			Method readMethod = sourcePd.getReadMethod();
			if (readMethod == null) {
				continue;
			}

			// 型情報
			Class< ? > targetWriteClass = writeMethod.getParameterTypes()[0];
			Class< ? > sourceReadClass = readMethod.getReturnType();

			try {
				// 読込メソッドがpublicでない場合、アクセス可能に設定
				if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
					readMethod.setAccessible(true);
				}
				// 読込メソッドを実行し、値を取得
				Object value = readMethod.invoke(source);

				// 書き込みメソッドがpublicでない場合、アクセス可能に設定
				if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
					writeMethod.setAccessible(true);
				}
				// 型が一致する場合、そのままセット
				// もしくは値が未設定の場合
				if (ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())
						|| value == null) {
					writeMethod.invoke(target, value);
				} else if (ClassUtils.isAssignable(sourceReadClass, String.class)
						&& ClassUtils.isAssignable(targetWriteClass, Short.class)) {
					// String → Short
					writeMethod.invoke(target, isBlank((String) value) ? null : Short.parseShort((String) value));
				} else if (ClassUtils.isAssignable(sourceReadClass, String.class)
						&& ClassUtils.isAssignable(targetWriteClass, Integer.class)) {
					// String → Integer
					writeMethod.invoke(target, isBlank((String) value) ? null : Integer.parseInt((String) value));
				} else if (ClassUtils.isAssignable(sourceReadClass, String.class)
						&& ClassUtils.isAssignable(targetWriteClass, BigDecimal.class)) {
					// String → BigDecimal
					writeMethod.invoke(target, isBlank((String) value) ? null : new BigDecimal((String) value));
				} else if (ClassUtils.isAssignable(sourceReadClass, BigDecimal.class)
						&& ClassUtils.isAssignable(targetWriteClass, String.class)) {
					// BigDecimal → String
					writeMethod.invoke(target, Objects.toString(value, ""));
				} else if (ClassUtils.isAssignable(sourceReadClass, Short.class)
						&& ClassUtils.isAssignable(targetWriteClass, String.class)) {
					// Short → String
					writeMethod.invoke(target, (String) value.toString());
				} else if (ClassUtils.isAssignable(sourceReadClass, Integer.class)
						&& ClassUtils.isAssignable(targetWriteClass, String.class)) {
					// Integer → String
					writeMethod.invoke(target, (String) value.toString());
				} else if (ClassUtils.isAssignable(sourceReadClass, Boolean.class)
						&& ClassUtils.isAssignable(targetWriteClass, String.class)) {
					// Boolean → String
					writeMethod.invoke(target, (String) value.toString());
				}
			} catch (Throwable ex) {
				throw new FatalBeanException(
						"Could not copy property '" + targetPd.getName() + "' from source to target", ex);
			}
		}
	}
}
