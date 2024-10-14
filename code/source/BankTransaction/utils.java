package BankTransaction;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.softwareag.util.IDataArray;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
// --- <<IS-END-IMPORTS>> ---

public final class utils

{
	// ---( internal utility methods )---

	final static utils _instance = new utils();

	static utils _newInstance() { return new utils(); }

	static utils _cast(Object o) { return (utils)o; }

	// ---( server methods )---




	public static final void calculateAvailableBalance (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(calculateAvailableBalance)>> ---
		// @sigtype java 3.5
		// [i] field:0:required sourceBalance
		// [i] field:0:required destinationBalance
		// [i] field:0:required amount
		// [o] object:0:required netSourceBalance
		// [o] object:0:required netDestinationBalance
 IDataCursor cursor = pipeline.getCursor();
    try {
        String sourceBalanceStr = IDataUtil.getString(cursor, "sourceBalance");
        String destinationBalanceStr = IDataUtil.getString(cursor, "destinationBalance");
        String amountStr = IDataUtil.getString(cursor, "amount");

        // Convert string values to BigDecimal
        BigDecimal sourceBalance = new BigDecimal(sourceBalanceStr);
        BigDecimal destinationBalance = new BigDecimal(destinationBalanceStr);
        BigDecimal amount = new BigDecimal(amountStr);

        // Calculate net balances
        BigDecimal netSourceBalance = sourceBalance.subtract(amount);
        BigDecimal netDestinationBalance = destinationBalance.add(amount);

        IDataUtil.put(cursor, "netSourceBalance", netSourceBalance);
        IDataUtil.put(cursor, "netDestinationBalance", netDestinationBalance);
        
    } catch (NumberFormatException e) {
        throw new ServiceException(e);
    } finally {
        cursor.destroy();
    }
		// --- <<IS-END>> ---

                
	}



	public static final void formatToString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(formatToString)>> ---
		// @sigtype java 3.5
		// [i] object:0:required account_expired
		// [i] object:0:required available_balance
		// [i] object:0:required create_date
		// [i] object:0:required birth_date
		// [o] field:0:required accountExpired
		// [o] field:0:required availableBalance
		// [o] field:0:required createDate
		// [o] field:0:required birthDate
		IDataCursor cursor = pipeline.getCursor();
		
		try {
			// input
		    Date accountExpired = (Date) IDataUtil.get(cursor, "account_expired");
		    BigDecimal availableBalance = (BigDecimal) IDataUtil.get(cursor, "available_balance");
		    Date createDate = (Date) IDataUtil.get(cursor, "create_date");
		    Date birthDate = (Date) IDataUtil.get(cursor, "birth_date");
		    		
		    // Format objects to string
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    SimpleDateFormat createDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String formattedAccountExpired = accountExpired != null ? dateFormat.format(accountExpired) : null;
		    String formattedAvailableBalance = availableBalance != null ? availableBalance.toString() : null;
		    String formattedBirthDate = birthDate != null ? dateFormat.format(birthDate) : null;
		    String formattedCreateDate = createDate != null ? createDateFormat.format(createDate) : null;
		
		    // output
		    IDataUtil.put(cursor, "accountExpired", formattedAccountExpired);
		    IDataUtil.put(cursor, "availableBalance", formattedAvailableBalance);
		    IDataUtil.put(cursor, "createDate", formattedCreateDate);
		    IDataUtil.put(cursor, "birthDate", formattedBirthDate);
		} catch (Exception e) {
		    throw new ServiceException(e);
		} finally {
		    cursor.destroy();
		}
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void generateAccountNumber (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(generateAccountNumber)>> ---
		// @sigtype java 3.5
		// [i] record:0:required getAccountSequenceOutput
		// [i] object:0:required birthDate
		// [o] field:0:required accountNumber
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    IData getSequenceOutput = IDataUtil.getIData(cursor, "getAccountSequenceOutput");
		    if (getSequenceOutput == null) {
		        throw new ServiceException("No sequence data found");
		    }
		    
		    IDataCursor getSequenceOutputCursor = getSequenceOutput.getCursor();
		
		    IData[] resultsList = IDataUtil.getIDataArray(getSequenceOutputCursor, "results");
		    if (resultsList == null || resultsList.length == 0) {
		        throw new ServiceException("No sequence results found");
		    }
		
		    IData sequenceResult = resultsList[0];
		    IDataCursor sequenceResultCursor = sequenceResult.getCursor();
		
		    // Get the value of 'nextval' from the result document
		    String nextval = IDataUtil.getString(sequenceResultCursor, "nextval");
		    if (nextval == null) {
		        throw new ServiceException("No 'nextval' value found");
		    }
		
		    // Pad the sequence with leading zeros to ensure it is 6 digits
		    nextval = String.format("%06d", Integer.parseInt(nextval));
		
		    Date birthDate = (Date) IDataUtil.get(cursor, "birthDate");
		
		    // Create Date Format
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		    String reformattedDateString = dateFormat.format(birthDate);
		
		    // Generate account number
		    String accountNumber = reformattedDateString + nextval;
		
		    IDataUtil.put(cursor, "accountNumber", accountNumber);
		
		} catch (Exception e) {
		    throw new ServiceException(e);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void generateExpiredDate (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(generateExpiredDate)>> ---
		// @sigtype java 3.5
		// [i] object:0:required createDate
		// [i] field:0:required expiredCount
		// [o] object:0:required expiredDate
		IDataCursor cursor = pipeline.getCursor();
		
		try {
			
			Date createDate = (Date) IDataUtil.get(cursor, "createDate");
			String expiredCountString = IDataUtil.getString(cursor, "expiredCount");
			int expiredCount = Integer.parseInt(expiredCountString);
		    	
		    // Calculate expired date
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(createDate);
		    calendar.add(Calendar.MONTH, expiredCount);
		    Date expiredDate = calendar.getTime();
		
		    IDataUtil.put(cursor, "expiredDate", expiredDate);
		    
		} catch (Exception e) {
		    throw new ServiceException(e);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void generateResponseId (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(generateResponseId)>> ---
		// @sigtype java 3.5
		// [i] record:0:required getResponseSequenceOutput
		// [o] field:0:required responseId
		IDataCursor cursor = pipeline.getCursor();
		
		try {
			// Generate timestamp (yyyyMMddHHmmss)
		    SimpleDateFormat sdfTimestamp = new SimpleDateFormat("yyyyMMddHHmmss");
		    String timestamp = sdfTimestamp.format(new Date());
		    
		    IData getSequenceOutput = IDataUtil.getIData(cursor, "getResponseSequenceOutput");
		    if (getSequenceOutput == null) {
		        throw new ServiceException("No sequence data found");
		    }
		    
		    IDataCursor getSequenceOutputCursor = getSequenceOutput.getCursor();
		
		    IData[] resultsList = IDataUtil.getIDataArray(getSequenceOutputCursor, "results");
		    if (resultsList == null || resultsList.length == 0) {
		        throw new ServiceException("No sequence results found");
		    }
		
		    IData sequenceResult = resultsList[0];
		    IDataCursor sequenceResultCursor = sequenceResult.getCursor();
		
		    // Get the value of 'nextval' from the result document
		    String nextval = IDataUtil.getString(sequenceResultCursor, "nextval");
		    if (nextval == null) {
		        throw new ServiceException("No 'nextval' value found");
		    }
			
		    // Pad sequence with leading zeros to ensure it is 6 digits
		    nextval = String.format("%06d", Integer.parseInt(nextval));
		
		    // Generate responseId
		    String responseId = "RES-" + timestamp + "-" + nextval;
			
			IDataUtil.put(cursor, "responseId", responseId);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			
			// Close cursor
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void parseBalanceValue (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(parseBalanceValue)>> ---
		// @sigtype java 3.5
		// [i] field:0:required balanceString
		// [o] object:0:required availableBalance
		IDataCursor cursor = pipeline.getCursor();
		
		try {
			String balanceString = IDataUtil.getString(cursor, "balanceString");
			BigDecimal balanceValue = null;
			
			if (balanceString == null) {
				balanceValue = new BigDecimal(0);
			} else {
				balanceValue = new BigDecimal(balanceString);
			}
			
			IDataUtil.put(cursor, "availableBalance", balanceValue);
			
		} catch (Exception e) {
		    throw new ServiceException(e);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void parseBirthDateFormat (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(parseBirthDateFormat)>> ---
		// @sigtype java 3.5
		// [i] field:0:required stringBirthDate
		// [o] object:0:required birthDate
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String stringBirthDate = IDataUtil.getString(cursor, "stringBirthDate");
		
		    // Parse stringBirthDate into Date object
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    Date date = sdf.parse(stringBirthDate);
		
		    IDataUtil.put(cursor, "birthDate", date);
		    
		} catch (Exception e) {
		    throw new ServiceException(e);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void parseDate (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(parseDate)>> ---
		// @sigtype java 3.5
		// [i] field:0:required dateString
		// [o] object:0:required date
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String dateString = IDataUtil.getString(cursor, "dateString");
		
		    // Parse stringBirthDate into Date object
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    Date date = sdf.parse(dateString);
		
		    IDataUtil.put(cursor, "date", date);
		    
		} catch (Exception e) {
		    throw new ServiceException(e);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void validateAccountExpired (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(validateAccountExpired)>> ---
		// @sigtype java 3.5
		// [i] field:0:required accountExpired
		// [o] field:0:required isExpired
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String accountExpiredStr = IDataUtil.getString(cursor, "accountExpired");
		
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    Date accountExpired = dateFormat.parse(accountExpiredStr);
		
		    Date currentDate = new Date();
		
		    // Validate accountExpired
		    boolean isExpired = accountExpired.before(currentDate);
		
		    String isExpiredString = String.valueOf(isExpired);
		    
		    IDataUtil.put(cursor, "isExpired", isExpiredString);
		} catch (ParseException e) {
		    throw new ServiceException(e);
		} finally {
		    // Close the cursor
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

