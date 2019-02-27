import Foundation

extension String {
    
    var localized: String {
        return localized()
    }
    
    var localizedOrEmpty: String {
        let value = localized()
        return value == self ? "" : value
    }
    
    fileprivate func localizedString(with comment: String, table: String? = nil) -> String {
        return NSLocalizedString(self, tableName: table, bundle: Bundle.main, value: "", comment: comment)
    }
    
    func capitalizingFirstLetter() -> String {
        return prefix(1).uppercased() + dropFirst()
    }
    
}

extension NSString {
    @objc var localized: String {
        return (self as String).localized()
    }
}
