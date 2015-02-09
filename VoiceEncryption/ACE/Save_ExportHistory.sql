USE [VoiceRecord3]
GO

/****** Object:  StoredProcedure [dbo].[Save_ExportHistory]    Script Date: 01/05/2015 14:58:12 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[Save_ExportHistory]
@UserID  varchar(50),
@TrackID int
AS
BEGIN
	INSERT INTO ExportHistory (ExportDate, trackID, UserID)values(GETDATE(), @TrackID, @UserID)
END

GO


